package com.loko.applications.dto.menu;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDto {
    private String id;
    private String title;
    private String url;
    private String icon;
    private boolean isGroup;
    private boolean isVisible;
    private int displayOrder;
    private String parentId;
    private Set<MenuDto> children;

    

}
