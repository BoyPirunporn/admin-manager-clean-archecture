package com.loko.infrastructure.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity implements UserDetails {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean isActive = true;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private RoleEntity role;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // ดึง ROLE_<NAME>
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().replace(" ", "_").toUpperCase()));
        }

        // // ดึง Permission ของแต่ละ Role
        // authorities.addAll(
        // roles.stream()
        // .flatMap(role -> role.getRoleMenuPermissions().stream())
        // .map(permission -> {
        // return new SimpleGrantedAuthority(permission.getMenuItem().getTitle() + "_" +
        // permission.getPermission().getName().replace(" ", "_").toUpperCase());
        // })
        // .collect(Collectors.toSet()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

}
