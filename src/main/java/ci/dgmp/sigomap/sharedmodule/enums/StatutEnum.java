package ci.dgmp.sigomap.sharedmodule.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatutEnum
{
    SAISIE("SAI", "Saisie"),
    SAISIE_CRT("SAI-CRT", "Saisie courtier"),
    EN_COURS_DE_REPARTITION("CREP", "En cours de répartition"),
    EN_ATTENTE_DE_PLACEMENT("APLA", "En attente de placement"),
    EN_COURS_DE_PLACEMENT("CPLA", "En cours de placement"),
    RETOURNE("RET", "Retournée"),
    EN_ATTENTE_DE_PAIEMENT("APAI", "En attente de paiement"),
    EN_COURS_DE_PAIEMENT("CPAI", "En cours de paiement"),
    EN_COURS_DE_REVERSEMENT("CREV", "En cours de reversement"),
    EN_COURS_DE_PAIEMENT_REVERSEMENT("CPAI-CREV", "En cours de paiement et de reversement"),
    SOLDE("SOLD", "Règlement soldée"),
    EN_ATTENTE_D_ARCHIVAGE("AARC", "En attente d'archivage"),
    ARCHIVE("ARC", "Archivé"),
    TRANSMIS("TRA", "Transmis"),

    EN_ATTENTE_DE_VALIDATION("AVAL", "En attente de validation"),
    VALIDE("VAL", "Validé"),
    EN_ATTENTE_DE_CONFIRMATION("ACONF", "En attente de confirmation"),
    REFUSE("REFUSE", "Réfusé"),
    ANNULE("ANNULE", "Annulé"),
    MODIFIE("MOD", "Modifié"),
    MAIL("MAIL", "Mail envoyé"),
    CREV("CREV", "En cours de reversement"),
    CPAI_CREV("CPAI-CREV", "En cours paiement et de reversement"),
    RETOURNER_VALIDATEUR("RET-VAL", "Retourné(e) par le validateur"),
    RETOURNER_COMPTABLE("RET-COMPTA", "Retourné(e) par le comptable"),
    ACCEPTE("ACCEPTE", "Accepté");

    public String staCode;
    public String staLibelle;
}
