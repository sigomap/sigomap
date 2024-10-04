package ci.dgmp.sigomap.modulestatut.entities;

import ci.dgmp.sigomap.sharedmodule.enums.TypeStatut;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import ci.dgmp.sigomap.sharedmodule.enums.TypeStatut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import ci.dgmp.sigomap.sharedmodule.enums.TypeStatut;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Statut {
  @Id
  private String staCode;
  private String staLibelle;
  private String staLibelleLong;
  @Enumerated
  private TypeStatut staType;
  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

    public Statut(String staCode) {
      this.staCode = staCode;
    }
}
