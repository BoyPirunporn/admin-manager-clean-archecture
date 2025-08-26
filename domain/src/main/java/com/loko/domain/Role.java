package com.loko.domain;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Role extends BaseModel{
    private String name;
    private String description;
    private Set<RolePermission> permissions;
    private int level;
}
