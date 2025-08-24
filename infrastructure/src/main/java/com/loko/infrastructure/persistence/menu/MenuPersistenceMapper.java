package com.loko.infrastructure.persistence.menu;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.loko.domain.Menu;
import com.loko.infrastructure.entities.MenuEntity;

@Mapper(componentModel = "spring")
public interface MenuPersistenceMapper {
    
    @Mapping(source="parent",target = "parent",qualifiedByName = "toParentDomain")
    Menu toDomain(MenuEntity entity);
    MenuEntity toEntity(Menu domain);

    @Named("toParentDomain")
    default Menu toParentDomain(MenuEntity parent){
        if(parent == null){
            return null;
        }
        Menu menu = new Menu();
        menu.setTitle(parent.getTitle());
        menu.setId(parent.getId().toString());
        return menu;
    }
}
