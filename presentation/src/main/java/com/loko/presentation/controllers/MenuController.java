package com.loko.presentation.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loko.applications.dto.ApiResponse;
import com.loko.applications.dto.menu.MenuDto;
import com.loko.applications.dto.menu.MenuPermissionNodeDto;
import com.loko.applications.ports.in.menu.MenuUseCase;

@RestController
@RequestMapping("${application.api.version}/menus")
public class MenuController {

    private final MenuUseCase menuUseCase;

    public MenuController(MenuUseCase menuUseCase) {
        this.menuUseCase = menuUseCase;
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<MenuPermissionNodeDto>>> getMenuWithRoleId() {
        return ResponseEntity.ok(ApiResponse.success(menuUseCase.getMenuByRoleId(), 200));
    }
    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<MenuDto>>> getMenuTree() {
        return ResponseEntity.ok(ApiResponse.success(menuUseCase.getMenuHierarchy(), 200));
    }

    @PostMapping
    public ResponseEntity<MenuDto> create(@RequestBody String entity) {
        return null;
    }

}
