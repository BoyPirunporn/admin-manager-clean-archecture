package com.loko.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RolePermission extends BaseModel {
    private Menu menu;
    private boolean canView = false;
    private boolean canCreate = false;
    private boolean canUpdate = false;
    private boolean canDelete = false;
    public RolePermission(Menu menu, boolean canView, boolean canCreate, boolean canUpdate, boolean canDelete) {
        this.menu = menu;
        this.canView = canView;
        this.canCreate = canCreate;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
    }

    
}
