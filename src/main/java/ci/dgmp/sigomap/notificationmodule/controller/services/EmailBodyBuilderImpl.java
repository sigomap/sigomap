package ci.dgmp.sigomap.notificationmodule.controller.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component @RequiredArgsConstructor
public class EmailBodyBuilderImpl implements EmailBodyBuilder
{
    @Value("${front.adress}")
    private String frontAddress;
    @Override
    public Map<String, String> buildTransmissionAffaireBody(String nomDestinataire, String nomCedante)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouvelle demande de placement de " + nomCedante;
        String corps = String.format(
                         "Vous venez de recevoir une nouvelle demande de placement de la part de %s.<br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                nomCedante,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public  Map<String, String> buildRetourAffaireBody(String nomDestinataire, String affCode, String motif)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Retour de votre affaire N°" + affCode;
        String corps = String.format(
                         "Votre affaire N°%s a été retournée par Nelson-RE pour le(s) motif(s) suivant(s) : <br/><br/>"
                        +"<b>%s</b><br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                affCode,
                motif,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public  Map<String, String> buildAffaireAttenteReglementBody(String nomDestinataire)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouvelle affaire en attente de règlement";
        String corps = String.format(
                        "Vous venez de recevoir une nouvelle affaire en attente de règlement.<br/><br/>" +
                        "Veuillez acceder à la plateforme <a href=\"%s\">sychronRE</a> pour d'éventuel(s) règlement(s) sur cette affaire.<br/><br/>" +
                        "Cordialement.",
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildPlacementEnAttenteDeValidationBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouvelle proposition de placement en attente de validation";
        String corps = String.format(
                        "Vous venez de recevoir une nouvelle proposition de placement en attente de validation.<br/>" +
                        "Il s'agit d'un placement sur l'affaire N°%s, auprès de %s, pour un montant de %f %s<br/><br/>"+
                        "Veuillez acceder à la plateforme <a href=\"%s\">sychronRE</a> pour le traitement de ce placement.<br/><br/>" +
                        "Cordialement.",
                 affCode, cesNom, repCapital == null ? 0 : repCapital.doubleValue(), devise,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildPlacementRetourneBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise, String motif) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Proposition de placement retournée par le validateur";
        String corps = String.format(
                       "La proposition de placement sur l'affaire N°%s a auprès de %s, d'un montant de %f %s a été retournée par le validateur pour le(s) motif(s) suivant(s):<br/>" +
                        "<b>%s</b><br/><br/>"+
                        "Veuillez acceder à la plateforme <a href=\"%s\">sychronRE</a> pour d'éventuel(s) traitements sur ce placement.<br/><br/>" +
                        "Cordialement.",
                 affCode, cesNom, repCapital == null ? 0 : repCapital.doubleValue(), devise, motif,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildPlacementEnAttenteDEnvoieDeNoteDeCessionnBody(String nomDestinataire, String affCode, String cesNom, BigDecimal repCapital, String devise)
    {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Proposition de placement validée";
        String corps = String.format(
                       "La proposition de placement sur l'affaire N°%s a auprès de %s, d'un montant de %f %s a été validée.<br/>" +
                        "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour l'envoie de la note de cession.<br/><br/>" +
                        "Cordialement.",
                 affCode, cesNom, repCapital == null ? 0 : repCapital.doubleValue(), devise,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildTransmissionSinistreBody(String nomDestinataire, String nomCedante) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouvelle declaration de sinistre de " + nomCedante;
        String corps = String.format(
                "Vous venez de recevoir une nouvelle declaration de sinistre de la part de %s.<br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                nomCedante,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildRetourSinistreBody(String nomDestinataire, String sinCode, String motif) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Retour de votre sinistre N°" + sinCode;
        String corps = String.format(
                "Votre sinistre N°%s a été retourné par Nelson-RE pour le(s) motif(s) suivant(s) : <br/><br/>"
                        +"<b>%s</b><br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                sinCode,
                motif,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }



    @Override
    public Map<String, String> buildSinistreEnAttenteDeValidationBody(String nomDestinataire) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouveau sinistre en attente de validation ";
        String corps = String.format(
                "Vous venez de recevoir un nouveau sinistre en attente de validation.<br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildRetourSinistreAuSouscripteurBody(String nomDestinataire, String sinCode, String motif) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Retour du sinistre N°" + sinCode;
        String corps = String.format(
                "Le sinistre N°%s a été retourné par le validateur pour le(s) motif(s) suivant(s) : <br/><br/>"
                        +"<b>%s</b><br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                sinCode,
                motif,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildSinistreEnAttenteDePaiementBody(String nomDestinataire) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Nouveau sinistre en attente de paiement ";
        String corps = String.format(
                "Vous venez de recevoir un nouveau sinistre en attente de paiement.<br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }

    @Override
    public Map<String, String> buildRetourSinistreAuValidateurBody(String nomDestinataire, String sinCode, String motif) {
        Map<String, String> emailBody = new HashMap<>();
        String objet = "Retour du sinistre N°" + sinCode;
        String corps = String.format(
                "Le sinistre N°%s a été retourné par le comptable pour le(s) motif(s) suivant(s) : <br/><br/>"
                        +"<b>%s</b><br/><br/>"
                        + "Veuillez accéder à la plateforme <a href=\"%s\">sychronRE</a> pour traiter la requête.<br/><br/>"
                        + "Cordialement.",
                sinCode,
                motif,
                frontAddress + "/Synchronre-Web#/authentication/signin"
        );
        emailBody.put("objet", objet);
        emailBody.put("corps", corps);
        return emailBody;
    }
}
