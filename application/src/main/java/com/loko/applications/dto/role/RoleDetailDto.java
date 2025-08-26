package com.loko.applications.dto.role;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDetailDto extends RoleDto {
    private Set<PermissionNodeDto> permissions = new HashSet<>();

    public RoleDetailDto() {
    }

    

    public RoleDetailDto(String id, String name, String description, int level, Set<PermissionNodeDto> permissions) {
        super(id, name, description, level);
        this.permissions = permissions;
    }



    public RoleDetailDto(String id, String name, String description, int level, LocalDateTime createdAt,
            LocalDateTime updatedAt, Set<PermissionNodeDto> permissions) {
        super(id, name, description, level, createdAt, updatedAt);
        this.permissions = permissions;
    }

   

    
}
