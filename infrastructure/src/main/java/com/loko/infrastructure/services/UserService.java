package com.loko.infrastructure.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.user.UserCreationDto;
import com.loko.applications.dto.user.UserDto;
import com.loko.applications.ports.in.user.UserUseCase;
import com.loko.applications.ports.out.email.EmailSenderRepositoryPort;
import com.loko.applications.ports.out.role.RoleRepositoryPort;
import com.loko.applications.ports.out.user.UserRepositoryPort;
import com.loko.applications.ports.out.verification.VerificationTokenRepositoryPort;
import com.loko.domain.Role;
import com.loko.domain.User;
import com.loko.domain.VerificationToken;
import com.loko.domain.exception.DuplicateResourceException;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.infrastructure.mapper.UserApiMapper;

@Service
@Transactional(readOnly = false)
public class UserService implements UserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final VerificationTokenRepositoryPort verificationTokenRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final EmailSenderRepositoryPort mailSender;
    private final UserApiMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.mail.verify.expiry}")
    private int verifyExpiry;

   

    public UserService(UserRepositoryPort userRepositoryPort, VerificationTokenRepositoryPort verificationTokenRepositoryPort,
            RoleRepositoryPort roleRepositoryPort, EmailSenderRepositoryPort mailSender, UserApiMapper mapper,
            PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.verificationTokenRepositoryPort = verificationTokenRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
        this.mailSender = mailSender;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserCreationDto dto) {
        if (userRepositoryPort.existsByEmail(dto.email())) {
            throw new DuplicateResourceException("Email '" + dto.email() + "' is already token.");
        }

        Role role = roleRepositoryPort.findById(dto.roleId())
                .orElseThrow(() -> new ResourceNotFoundException("One or more role IDs are invalid."));

        User user = mapper.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(role);
        user.setActive(false);
        user.setEmailVerify(false);

        User saveUser = userRepositoryPort.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(Instant.now().plusSeconds(verifyExpiry));
        verificationToken.setToken(token);
        verificationToken.setUser(saveUser);
        verificationTokenRepositoryPort.save(verificationToken);

        // send email
        mailSender.sendVerificationEmail("template-default",saveUser.getEmail(), saveUser.getFirstName() + " " + saveUser.getLastName(), token);
        
        return mapper.toUserDto(saveUser);
    }

    @Override
    public PagedResult<UserDto> getAllUser(PageQuery query) {
        PagedResult<User> pageResult = userRepositoryPort.findPageable(query);

        List<UserDto> toDtos = pageResult.data().stream().map(mapper::toUserDto).toList();

        return new PagedResult<>(
                toDtos,
                pageResult.totalElements(),
                pageResult.totalPages(),
                pageResult.pageNumber(),
                pageResult.pageSize(),
                pageResult.isLast());
    }

    @Override
    public UserCreationDto getUserById(String id) {
        return userRepositoryPort.findById(id).map(mapper::toUserCreationDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
