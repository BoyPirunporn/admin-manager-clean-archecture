package com.loko.infrastructure.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity extends BaseEntity {
    private String name;
    private String description;
    @Column(columnDefinition = "int default 2")
    private int level = 2;
    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<RolePermissionEntity> permissions;
}
