package ci.dgmp.sigomap.authmodule.model.entities;

import ci.dgmp.sigomap.typemodule.model.entities.Type;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Audited @EntityListeners(AuditingEntityListener.class)
public class AppFunction extends HistoDetails
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FNC_ID_GEN")
    @SequenceGenerator(name = "FNC_ID_GEN", sequenceName = "FNC_ID_GEN", allocationSize = 10)
    protected Long id;

    private String name;
    @ManyToOne @JoinColumn(name = "USER_ID")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "TYPE_CODE")
    private Type type;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;

    public AppFunction(String name, AppUser user, int fncStatus, LocalDate startsAt, LocalDate endsAt) {
        this.name = name;
        this.user = user;
        this.fncStatus = fncStatus;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public AppFunction(Long fncId) {
        this.id = fncId;
    }
    @Override
    public String toString() {
        return id + "_" + name;
    }
}
