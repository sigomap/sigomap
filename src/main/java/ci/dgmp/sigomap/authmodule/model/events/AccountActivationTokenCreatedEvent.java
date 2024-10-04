package ci.dgmp.sigomap.authmodule.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ci.dgmp.sigomap.authmodule.model.entities.AccountToken;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;
import ci.dgmp.sigomap.authmodule.model.entities.AppUser;

@Getter
public class AccountActivationTokenCreatedEvent extends ApplicationEvent
{
    private AccountToken accountToken;
    private AppUser user;
    private ActionIdentifier ai;

    public AccountActivationTokenCreatedEvent(Object source, AccountToken token, AppUser user, ActionIdentifier ai) {
        super(source);
        this.accountToken = token;
        this.user = user;
        this.ai = ai;
    }
}