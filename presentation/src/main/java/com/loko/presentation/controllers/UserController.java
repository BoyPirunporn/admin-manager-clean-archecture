package com.loko.presentation.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.loko.applications.dto.ApiResponse;
import com.loko.applications.dto.PageQuery;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.user.UserCreationDto;
import com.loko.applications.dto.user.UserDto;
import com.loko.applications.ports.in.user.UserUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("${application.api.version}/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping
    public ResponseEntity<PagedResult<UserDto>> getAllUser(
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            @RequestParam(required = false) String search) {
        PageQuery query = new PageQuery(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().stream().findFirst().map(Sort.Order::getProperty).orElse("username"),
                pageable.getSort().stream().findFirst().map(Sort.Order::getDirection).orElse(Sort.Direction.ASC).name(),
                search);
        return ResponseEntity.ok(userUseCase.getAllUser(query));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserCreationDto>> getUserById(@PathVariable(name = "id",required = true) String id) {
        return ResponseEntity.ok(ApiResponse.success(userUseCase.getUserById(id),200));
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserCreationDto request) {
        return ResponseEntity.ok(userUseCase.createUser(request));
    }

}
