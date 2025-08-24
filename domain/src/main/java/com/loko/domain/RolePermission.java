package com.loko.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermission {
    private String id;
    private Menu menu;
    private boolean canView = false;
    private boolean canCreate = false;
    private boolean canUpdate = false;
    private boolean canDelete = false;
}
