package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import ci.dgmp.sigomap.authmodule.controller.services.spec.IFunctionService;
import ci.dgmp.sigomap.authmodule.model.entities.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper
{
    @Autowired protected IFunctionService functionService;

    @Mapping(target="active", expression="java(false)")
    @Mapping(target="notBlocked", expression="java(true)")
    public abstract AppUser mapToUser(CreateUserDTO dto);

    @Mapping(target="active", expression="java(true)")
    @Mapping(target="notBlocked", expression="java(true)")
    public abstract AppUser mapToUser(CreateActiveUserDTO dto);

    @Mapping(target = "civilite", source = "civilite.name")
    @Mapping(target = "nationalite", source = "nationalite.nationalite")
    public abstract ReadUserDTO mapToReadUserDTO(AppUser user);

}