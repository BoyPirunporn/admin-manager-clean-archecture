package com.loko.applications.dto.role;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto{
    String id;
    String name;
    String description;
    int level;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    public RoleDto(String id, String name, String description,int level) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = level;
    }

    
} 