package com.loko.presentation.controllers;

import org.springframework.data.domain.Pageable;
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
import com.loko.applications.dto.ApiResponseMessage;
import com.loko.applications.dto.PagedResult;
import com.loko.applications.dto.user.UserCreationDto;
import com.loko.applications.dto.user.UserDto;
import com.loko.applications.ports.in.user.UserUseCase;
import com.loko.presentation.helper.PageablePresenHelper;

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
            @RequestParam(name="search",required = false) String search,
            @RequestParam(name="searchBy",required = false) String searchBy) {
        
        return ResponseEntity.ok(userUseCase.getAllUser(PageablePresenHelper.buildPageQuery(pageable, searchBy,search)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<UserCreationDto>> getUserById(@PathVariable(name = "id",required = true) String id) {
        return ResponseEntity.ok(ApiResponse.success(userUseCase.getUserById(id),200));
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseMessage> create(@Valid @RequestBody UserCreationDto request) {
        userUseCase.createUser(request);
        return ResponseEntity.ok(new ApiResponseMessage(201,"Your account has been created! Please check your email to verify your account before using the system."));
    }

}
