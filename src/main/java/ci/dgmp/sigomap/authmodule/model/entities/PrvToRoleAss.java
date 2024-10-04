package ci.dgmp.sigomap.authmodule.model.entities;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @DiscriminatorValue("PRV_TO_ROLE")
@Audited @EntityListeners(AuditingEntityListener.class)
public class PrvToRoleAss extends Assignation
{
    @ManyToOne @JoinColumn(name = "PRV_ID")
    private AppPrivilege privilege;
    @ManyToOne @JoinColumn(name = "ROLE_ID")
    private AppRole role;

    public PrvToRoleAss(Long assId, int assStatus, LocalDate startsAt, LocalDate endsAt, AppPrivilege privilege, AppRole role) {
        super(assId, assStatus, startsAt, endsAt);
        this.privilege = privilege;
        this.role = role;
    }
}
