package ci.dgmp.sigomap.authmodule.model.dtos.approle;

import ci.dgmp.sigomap.authmodule.model.entities.AppRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper
{
    AppRole mapToRole(CreateRoleDTO dto);
    ReadRoleDTO mapToReadRoleDTO(AppRole role);
}
