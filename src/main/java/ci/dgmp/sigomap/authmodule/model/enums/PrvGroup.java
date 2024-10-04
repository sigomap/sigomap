package ci.dgmp.sigomap.authmodule.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public enum PrvGroup
{
    SECURITY("Sécurité"),
    ADMINISTRATION("Administration"),
    STR_POST("Structures et postes de travail"),
    AGENT("Agents"),
    MOUVEMENT("Mouvement"),
    PROMOTION("Promotion"),
    NOMINATION("Nomination");

    private String group;
}
