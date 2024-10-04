package ci.dgmp.sigomap.authmodule.model.entities;

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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Audited @EntityListeners(AuditingEntityListener.class)
public class AppRole extends HistoDetails
{
    @Id
    private String roleCode;
    @Column(unique = true)
    private String roleName;
    private String prvAuthorizedTypes;

    public AppRole(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public AppRole(String roleCode) {
        this.roleCode = roleCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppRole)) return false;
        AppRole appRole = (AppRole) o;
        return Objects.equals(roleCode, appRole.getRoleCode()) || Objects.equals(getRoleName(), appRole.getRoleName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleCode, roleName);
    }

    @Override
    public String toString() {
        return roleCode + '_' +roleName;
    }
}
