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
    private String nameEN;
    private String nameTH;
    private String url;
    private String icon;
    private boolean isVisible;
    private boolean isGroup;
    private int displayOrder;
    private Menu parent;
    private Set<Menu> children = new TreeSet<>(Comparator.comparingInt(Menu::getDisplayOrder));
    
    public Menu(String nameEN, String nameTH, String url, String icon, boolean isVisible, boolean isGroup,
            int displayOrder) {
        this.nameEN = nameEN;
        this.nameTH = nameTH;
        this.url = url;
        this.icon = icon;
        this.isVisible = isVisible;
        this.isGroup = isGroup;
        this.displayOrder = displayOrder;
    }

    public Menu(String nameEN, String nameTH, String url, String icon, boolean isVisible, boolean isGroup,
            int displayOrder, Menu parent) {
        this.nameEN = nameEN;
        this.nameTH = nameTH;
        this.url = url;
        this.icon = icon;
        this.isVisible = isVisible;
        this.isGroup = isGroup;
        this.displayOrder = displayOrder;
        this.parent = parent;
    }
    
    
    

    

    
}
