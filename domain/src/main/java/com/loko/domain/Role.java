package com.loko.domain;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseModel{
    private String name;
    private String description;
    private Set<RolePermission> permissions;
    private int level;
    public Role(String name, String description, int level) {
        this.name = name;
        this.description = description;
        this.level = level;
    }
    public Role(String name, String description, Set<RolePermission> permissions, int level) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.level = level;
    }
    

}
