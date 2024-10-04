package ci.dgmp.sigomap.sharedmodule.enums;

import lombok.Getter;

public enum PersStatus
{
    ACTIVE("Actif"),
    DELETED("Suppimé"),
    STORED("Historisé");
    @Getter
    public String name;
    PersStatus(String name) {
        this.name = name;
    }
}
