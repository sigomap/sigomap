package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AssignationDTO
{
    private Long assignationId;
    private Long userId;
    private String username;
    private Long privilegeId;
    private String privilegeName;
    private Long roleId;
    private String roleName;
    private Long functionId;
    private String functionName;
    private boolean active;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
