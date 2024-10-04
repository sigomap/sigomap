package ci.dgmp.sigomap.authmodule.controller.services.spec;

import org.springframework.transaction.event.TransactionalEventListener;
import ci.dgmp.sigomap.authmodule.model.entities.AccountToken;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;
import ci.dgmp.sigomap.authmodule.model.entities.AppUser;

public interface IAccountTokenService
{
    AccountToken createAccountToken(AppUser appUser, ActionIdentifier ai);
    AccountToken createAccountToken(Long agentId, ActionIdentifier ai);
}
