package ci.dgmp.sigomap.authmodule.controller.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IActionIdentifierService;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IJwtService;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;

import java.util.UUID;

@Service @RequiredArgsConstructor
public class ActionIdentifierService implements IActionIdentifierService
{
    private final IJwtService jwtService;
    @Override
    public ActionIdentifier getActionIdentifierFromSecurityContext(String actionName)
    {
        String actionId = UUID.randomUUID().toString();
        String connectionId = jwtService.getCurrentJwt() == null ? null : jwtService.getJwtInfos().getConnectionId();

        ActionIdentifier actionIdentifier = ActionIdentifier
                .builder()
                .actionId(actionId).actionName(actionName).connexionId(connectionId)
                .build();
        return actionIdentifier;
    }
}
