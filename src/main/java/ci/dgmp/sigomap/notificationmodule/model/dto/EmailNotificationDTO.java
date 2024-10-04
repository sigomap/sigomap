package ci.dgmp.sigomap.notificationmodule.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmailNotificationDTO
{
    private Long mailId;
    private String username;
    private String email;
    private String token; /** Opaque token */
    private LocalDateTime sendingDate;
    private boolean seen;
    private boolean sent;
    private String mailObject;
    private String mailMessage;

    private Long senderUserId;

    private String systemMailSender; /* L'adresse mail utilisée pour envoyer le mail*/
}
