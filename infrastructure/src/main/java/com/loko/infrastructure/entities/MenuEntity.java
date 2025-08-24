package com.loko.infrastructure.entities;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "menus")
public class MenuEntity extends BaseEntity{
    private String title;
    private String url;
    private String icon;
    private boolean isVisible = false;
    private boolean isGroup = false;
    private int displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuEntity parent;

    @OneToMany(mappedBy = "parent",fetch = FetchType.EAGER)
    private Set<MenuEntity> children;

    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public MenuEntity getParent() {
        return parent;
    }

    public void setParent(MenuEntity parent) {
        this.parent = parent;
    }

    public Set<MenuEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<MenuEntity> children) {
        this.children = children;
    }

    
    
}
