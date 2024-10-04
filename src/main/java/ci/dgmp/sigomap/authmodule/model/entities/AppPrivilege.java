package ci.dgmp.sigomap.authmodule.model.entities;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Audited @EntityListeners(AuditingEntityListener.class)
public class AppPrivilege extends HistoDetails
{
    @Id
    private String privilegeCode;
    @Column(unique = true, columnDefinition = "text")
    private String privilegeName;
    @ManyToOne @JoinColumn
    private Type prvType;

    public AppPrivilege(String privilegeCode)
    {
        this.privilegeCode = privilegeCode;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o==null) return false;
        if(!(o instanceof AppPrivilege)) return false;
        AppPrivilege that = (AppPrivilege) o;
        return privilegeCode.equals(that.privilegeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(privilegeCode);
    }
    @Override
    public String toString() {
        return privilegeCode + "_" + privilegeName;
    }
}
