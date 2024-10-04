package ci.dgmp.sigomap.authmodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public enum UserStatus
{
    ACTIVE("Actif"),
    BLOCKED("Bloqué"),
    STANDING_FOR_ACCOUNT_ACTIVATION("En attente d'activation du compte"),
    UN_KNOWN("Inconnu"),
    STANDING_FOR_ACCOUNT_ACTIVATION_LINK("En attente d'un lien d'activation");

    private String status;
}
