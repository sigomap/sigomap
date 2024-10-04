package ci.dgmp.sigomap.authmodule.controller.services.spec;

import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;
import ci.dgmp.sigomap.authmodule.model.entities.HistoDetails;

public interface IActionIdentifierService
{
    ActionIdentifier getActionIdentifierFromSecurityContext(String actionName);
}
