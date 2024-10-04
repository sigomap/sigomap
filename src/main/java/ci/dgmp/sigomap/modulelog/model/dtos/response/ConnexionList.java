package ci.dgmp.sigomap.modulelog.model.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ConnexionList {
    private Long id;
    private Long userId;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String action;
    private LocalDateTime actionDateTime;
    private String ipAddress;
    private String hostName;
    private String connectionId;
    protected Long foncId;
    private String foncName;

    private String errorMessage;
    private String stackTrace;

    public ConnexionList(Long id, Long userId, String userEmail, String firstName, String lastName, String action, LocalDateTime actionDateTime, String ipAddress, String hostName, String connectionId, Long foncId, String foncName) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.action = action;
        this.actionDateTime = actionDateTime;
        this.ipAddress = ipAddress;
        this.hostName = hostName;
        this.connectionId = connectionId;
        this.foncId = foncId;
        this.foncName = foncName;
    }
}
