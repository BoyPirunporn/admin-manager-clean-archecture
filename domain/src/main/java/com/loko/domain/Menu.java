package com.loko.domain;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends BaseModel{
    private String title;
    private String url;
    private String icon;
    private boolean isVisible;
    private boolean isGroup;
    private int displayOrder;
    private Menu parent;
    private Set<Menu> children = new TreeSet<>(Comparator.comparingInt(Menu::getDisplayOrder));
}
