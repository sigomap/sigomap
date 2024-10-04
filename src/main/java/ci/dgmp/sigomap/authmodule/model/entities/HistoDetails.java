package ci.dgmp.sigomap.authmodule.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@MappedSuperclass
@Audited @EntityListeners(AuditingEntityListener.class)
public class HistoDetails
{
    protected String actionName;
    protected String actionId;
    protected String connexionId;
    @CreatedDate
    @Column(name = "CreatedAt")
    protected LocalDateTime createdAt;
    @CreatedBy
    @Column(name = "CreatedBy", length = 50)
    protected String createdBy;
    @LastModifiedDate
    @Column(name = "UpdatedAt")
    protected LocalDateTime updatedAt;
    @LastModifiedBy
    @Column(name = "UpdatedBy", length = 50)
    protected String updatedBy;
    @Column(name = "DeletedAt")
    protected LocalDateTime deletedAt;
    @Column(name = "DeletedBy", length = 50)
    protected String deletedBy;
    @Column(name = "isDeleted", length = 50)
    protected Boolean isDeleted = false;
}
