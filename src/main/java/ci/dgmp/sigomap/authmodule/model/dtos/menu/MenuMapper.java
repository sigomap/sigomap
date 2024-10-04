package ci.dgmp.sigomap.authmodule.model.dtos.menu;

import ci.dgmp.sigomap.authmodule.model.entities.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuMapper
{
    @Mapping(target = "status", expression = "java(ci.dgmp.sigomap.sharedmodule.enums.PersStatus.ACTIVE)")
    Menu mapToMenu(CreateMenuDTO dto);
}