package com.loko.infrastructure.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.user.UserCreationDto;
import com.loko.applications.dto.user.UserDto;
import com.loko.applications.ports.in.user.UserUseCase;
import com.loko.applications.ports.out.role.RoleRepositoryPort;
import com.loko.applications.ports.out.user.UserRepositoryPort;
import com.loko.domain.Role;
import com.loko.domain.User;
import com.loko.domain.exception.DuplicateResourceException;
import com.loko.domain.exception.ResourceNotFoundException;
import com.loko.infrastructure.mapper.UserApiMapper;

@Service
@Transactional(readOnly = false)
public class UserService implements UserUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final RoleRepositoryPort roleRepositoryPort;
    private final UserApiMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort,
            UserApiMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
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
        user.setActive(true);

        User saveUser = userRepositoryPort.save(user);
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
