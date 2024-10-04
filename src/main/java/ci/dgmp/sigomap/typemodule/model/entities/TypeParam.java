package ci.dgmp.sigomap.typemodule.model.entities;

import ci.dgmp.sigomap.sharedmodule.enums.PersStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Table(name = "type_param") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class TypeParam
{
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TYPE_ID_GEN")
    @SequenceGenerator(name = "TYPE_ID_GEN", sequenceName = "TYPE_ID_GEN", allocationSize = 10)
    private Long typeParamId;
    @ManyToOne @JoinColumn(name = "parent_code")
    private Type parent;
    @ManyToOne @JoinColumn(name = "child_code")
    private Type child;
    @Enumerated(EnumType.STRING)
    private PersStatus status;
}