package ci.dgmp.sigomap.authmodule.controller.services.spec;

import ci.dgmp.sigomap.authmodule.model.dtos.approle.CreateRoleDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.approle.ReadRoleDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;

import java.net.UnknownHostException;
import java.util.List;

public interface IRoleService
{
    ReadRoleDTO createRole(CreateRoleDTO dto) throws UnknownHostException;
    Page<ReadRoleDTO> searchRoles(String searchKey, Pageable pageable);

    @Transactional
    ReadRoleDTO updateRole(PrvsToRoleDTO dto) throws UnknownHostException;

    List<SelectOption> getRoleOptions(List<String> roleCodes);
}
