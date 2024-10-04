package ci.dgmp.sigomap.notificationmodule.controller.services;

import java.math.BigDecimal;
import java.util.Map;

public interface EmailBodyBuilder
{
    Map<String, String> buildTransmissionAffaireBody(String nomDestinataire, String nomCedante);
    Map<String, String>  buildRetourAffaireBody(String nomDestinataire, String affCode, String motif);

    Map<String, String> buildAffaireAttenteReglementBody(String nomDestinataire);

    Map<String, String> buildPlacementEnAttenteDeValidationBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise);

    Map<String, String> buildPlacementRetourneBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise, String motif);

    Map<String, String> buildPlacementEnAttenteDEnvoieDeNoteDeCessionnBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise);

    Map<String, String> buildTransmissionSinistreBody(String nomDestinataire, String nomCedante);
    Map<String, String>  buildRetourSinistreBody(String nomDestinataire, String sinCode, String motif);
    Map<String, String> buildSinistreEnAttenteDeValidationBody(String nomDestinataire);
    Map<String, String>  buildRetourSinistreAuSouscripteurBody(String nomDestinataire, String sinCode, String motif);

    Map<String, String> buildSinistreEnAttenteDePaiementBody(String nomDestinataire);
    Map<String, String>  buildRetourSinistreAuValidateurBody(String nomDestinataire, String sinCode, String motif);
}
