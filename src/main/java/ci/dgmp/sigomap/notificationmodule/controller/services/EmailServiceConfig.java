package ci.dgmp.sigomap.notificationmodule.controller.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data @Component
public class EmailServiceConfig
{
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Value("${spring.mail.password}")
    private String senderPassword;
    @Value("${auth.activate-account-link}")
    private String activateAccountLink;
    @Value("${auth.reinit-password-link}")
    private String reinitPasswordLink;

    @Value("${auth.server.address}")
    private String clientAddress; // clientAddress == authServerAddress car nous sommes en présence d'une application Monolithique

    @Value("${auth.server.address}")
    private String authServerAddress;
}