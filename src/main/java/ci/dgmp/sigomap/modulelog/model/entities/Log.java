package ci.dgmp.sigomap.modulelog.model.entities;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ci.dgmp.sigomap.authmodule.model.entities.AppFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Inheritance(strategy = InheritanceType.JOINED)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Log
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_ID_GEN")
    @SequenceGenerator(name = "LOG_ID_GEN", sequenceName = "LOG_ID_GEN", allocationSize = 10)
    private Long id;
    private Long userId;
    private String userEmail;
    private String action;
    @CreationTimestamp
    private LocalDateTime actionDateTime;
    private String ipAddress;
    private byte[] macAddress;
    private String hostName;
    private String connectionId;
    private Long mainActionId;
    @ManyToOne @JoinColumn(name = "FUNC_ID")
    private AppFunction function;
    @Column(length = 500000)
    private String errorMessage;
    @Column(length = 500000)
    private String stackTrace;
    @Transient
    private List<LogDetails> logDetails;
}
