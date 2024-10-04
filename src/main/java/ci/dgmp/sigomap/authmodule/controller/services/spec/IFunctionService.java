package ci.dgmp.sigomap.authmodule.controller.services.spec;

import org.springframework.data.domain.Page;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.CreateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.ReadFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appuser.AuthResponseDTO;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;

import java.net.UnknownHostException;

public interface IFunctionService
{
    Long getActiveCurrentFunctionId(Long userId);
    String getActiveCurrentFunctionName(Long userId);
    ReadFncDTO createFnc(CreateFncDTO dto, ActionIdentifier ai);
    AuthResponseDTO setFunctionAsDefault(Long fncId, ActionIdentifier ai);
    void revokeFunction(Long fncId, ActionIdentifier ai);
    void restoreFunction(Long fncId, ActionIdentifier ai);
    //ReadFncDTO setFunctionAuthorities(SetAuthoritiesToFunctionDTO dto);
    ReadFncDTO updateFunction(UpdateFncDTO dto, ActionIdentifier ai) throws UnknownHostException;

    ReadFncDTO getActiveCurrentFunction(Long userId);

    ReadFncDTO getFunctioninfos(Long foncId);

    Page<ReadFncDTO> search(Long userId, String key, int page, int size, boolean withRevoked);

}
