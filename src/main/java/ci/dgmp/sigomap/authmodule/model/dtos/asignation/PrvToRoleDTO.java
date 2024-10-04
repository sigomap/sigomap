package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ExistingPrivilegeCode;
import ci.dgmp.sigomap.authmodule.model.dtos.approle.ExistingRoleCode;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrvToRoleDTO
{
    @ExistingRoleCode
    private Long roleId;
    @ExistingPrivilegeCode
    private Long privilegeId;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private boolean permanent;
}
