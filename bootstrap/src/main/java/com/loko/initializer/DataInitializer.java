package com.loko.initializer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.loko.applications.ports.out.menu.MenuRepositoryPort;
import com.loko.applications.ports.out.role.RoleRepositoryPort;
import com.loko.applications.ports.out.user.UserRepositoryPort;
import com.loko.domain.Menu;
import com.loko.domain.Role;
import com.loko.domain.RolePermission;
import com.loko.domain.User;
import com.loko.domain.exception.ResourceNotFoundException;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final RoleRepositoryPort roleRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final MenuRepositoryPort menuRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.initial-data.default.super-role-name:SUPER_ADMIN}")
    private String ROLE_SUPER_ADMIN;
    @Value("${application.initial-data.default.user.email:admin@admin.com}")
    private String USER_EMAIL;
    @Value("${application.initial-data.default.user.password:P@ssw0rd}")
    private String USER_PASSWORD;
    @Value("${application.initial-data.default.user.firstName:Super}")
    private String FIRST_NAME;
    @Value("${application.initial-data.default.user.lastName:Administrator}")
    private String LAST_NAME;

    public DataInitializer(RoleRepositoryPort roleRepositoryPort, UserRepositoryPort userRepositoryPort,
            MenuRepositoryPort menuRepositoryPort, PasswordEncoder passwordEncoder) {
        this.roleRepositoryPort = roleRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.menuRepositoryPort = menuRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("============================================================");
        logger.info("[DataInitializer] Starting database initialization...");
        logger.info("============================================================");

        createMenuIfNotFound();
        createRoleIfNotFound();
        createSuperUserIfNotFound();

        logger.info("============================================================");
        logger.info("[DataInitializer] Database initialization finished successfully.");
        logger.info("============================================================");
    }

    private void createMenuIfNotFound() {
        logger.info("[DataInitializer] Step 1: Checking for existing menus...");
        if (menuRepositoryPort.count() == 0) {
            logger.info(" -> No menus found. Creating default menu structure...");

            menuRepositoryPort.save(new Menu("Dashboard", "/dashboard", "LayoutDashboard", true, true, 1));
            Menu settings = menuRepositoryPort.save(new Menu("Settings", "/settings", "Settings", true, true, 2));

            menuRepositoryPort.saveAll(
                    List.of(
                            new Menu("Members", "/settings/member", "Users", true, false, 1, settings),
                            new Menu("Roles", "/settings/roles", "UserCog", true, false, 2, settings),
                            new Menu("Logs", "/settings/logs", "History", true, false, 3, settings)));
            logger.info(" -> Default menus created successfully.");

        } else {
            logger.info(" -> Menus already exist. Skipping creation.");
        }
    }

    private void createRoleIfNotFound() {
        logger.info("[DataInitializer] Step 2: Checking for {} role...", ROLE_SUPER_ADMIN);
        if (roleRepositoryPort.findByNameWithPermissions(ROLE_SUPER_ADMIN).isEmpty()) {
            logger.info(" -> {} role not found. Creating role with full permissions...", ROLE_SUPER_ADMIN);

            List<Menu> allMenus = menuRepositoryPort.findAllWithParent();
            Set<RolePermission> allPermissions = allMenus.stream().map(menu -> {
                if (menu.isGroup()) {
                    return new RolePermission(menu, true, false, false, false);
                }
                return new RolePermission(menu, true, true, true, true);
            }).collect(Collectors.toSet());

            Role superRole = new Role(ROLE_SUPER_ADMIN, "Super Administrator with all permissions", allPermissions, 1);

            roleRepositoryPort.save(superRole);
            logger.info(" -> SUPER_ADMIN role created successfully.");
        } else {

            logger.info(" -> SUPER_ADMIN role already exists. Skipping creation.");
        }
    }

    private void createSuperUserIfNotFound() {
        logger.info("[DataInitializer] Step 3: Checking for SUPER_ADMIN user... ");
        if (!userRepositoryPort.existsByRole_Name(ROLE_SUPER_ADMIN)) {
            logger.info(" -> No super admin user found. Creating default super admin user ...{}", USER_EMAIL);
            Role superRole = roleRepositoryPort.findByNameWithPermissions(ROLE_SUPER_ADMIN)
                    .orElseThrow(() -> new ResourceNotFoundException(ROLE_SUPER_ADMIN));
            logger.info("SUPER ROLE {}", superRole.getId());
            User user = new User();

            user.setRole(superRole);
            user.setEmail(USER_EMAIL);
            user.setActive(true);
            user.setPassword(passwordEncoder.encode(USER_PASSWORD));
            user.setFirstName(FIRST_NAME);
            user.setLastName(LAST_NAME);
            user.setEmailVerify(true);

            userRepositoryPort.save(user);
            logger.info(" -> Default super admin user (email: {}) created successfully.", USER_EMAIL);
        } else {
            logger.info(" -> A user with SUPER_ADMIN role already exists. Skipping creation.");
        }
        ;
    }

}
