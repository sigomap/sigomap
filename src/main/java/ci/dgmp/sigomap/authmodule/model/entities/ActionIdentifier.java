package ci.dgmp.sigomap.authmodule.model.entities;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActionIdentifier
{
    protected String actionName;
    protected String actionId;
    protected String connexionId;
}
