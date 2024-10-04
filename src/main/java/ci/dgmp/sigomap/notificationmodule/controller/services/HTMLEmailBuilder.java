package ci.dgmp.sigomap.notificationmodule.controller.services;

public interface HTMLEmailBuilder
{
    String buildAccountActivationHTMLEmail(String recipientUsername, String link);
    String buildPasswordReinitialisationHTMLEmail(String recipientUsername, String link);
    String buildGenericEmail(String objet, String destinataire, String corps);
}
