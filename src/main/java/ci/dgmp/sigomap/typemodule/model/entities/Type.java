package ci.dgmp.sigomap.typemodule.model.entities;

import ci.dgmp.sigomap.sharedmodule.enums.PersStatus;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.List;

@Table(name = "type") @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Type
{
    @Id @Column(nullable = false, unique = true)
    private String uniqueCode;
    @Enumerated(EnumType.STRING)
    private TypeGroup typeGroup;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private PersStatus status;
    @Transient
    private List<Type> children;
    private String objectFolder;

    @Override
    public String toString() {
        return name + " (" +uniqueCode + ")"  ;
    }

    public Type(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}