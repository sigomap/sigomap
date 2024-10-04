package ci.dgmp.sigomap.authmodule.model.entities;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
public class AccountToken extends HistoDetails
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TOKEN_ID_GEN")
    @SequenceGenerator(name = "TOKEN_ID_GEN", sequenceName = "TOKEN_ID_GEN", allocationSize = 10)
    private Long tokenId;
    @Column(unique = true)
    private String token;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;
    private LocalDateTime usageDate;
    private boolean alreadyUsed;
    private Long agentId; // Très important pour lier le token à un agent (acteur) au cas où user est null
    private String password; // Mot de passe d'utilisation du token aléatoirement généré!
    private boolean emailSent;
    @ManyToOne
    private AppUser user;

    public AccountToken(Long tokenId)
    {
        this.tokenId = tokenId;
    }

    public AccountToken(String token, AppUser user)
    {
        this.token = token;
        this.emailSent = false;
        this.alreadyUsed = false;
        this.user = user;
        this.creationDate = LocalDateTime.now();
        this.expirationDate = LocalDateTime.now().plusDays(1);
    }
}
