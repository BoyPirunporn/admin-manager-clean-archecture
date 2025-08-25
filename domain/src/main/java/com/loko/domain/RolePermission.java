package com.loko.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermission extends BaseModel {
    private Menu menu;
    private boolean canView = false;
    private boolean canCreate = false;
    private boolean canUpdate = false;
    private boolean canDelete = false;
}
