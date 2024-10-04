package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import ci.dgmp.sigomap.authmodule.model.dtos.approle.ExistingRoleCode;
import ci.dgmp.sigomap.authmodule.model.dtos.approle.UniqueRoleCode;
import ci.dgmp.sigomap.authmodule.model.dtos.approle.UniqueRoleName;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@UniqueRoleName
public class PrvsToRoleDTO
{
    private String roleName;
    @ExistingRoleCode
    private String roleCode;
    private Set<String> prvCodes = new HashSet<>();
    private LocalDate startsAt;
    private LocalDate endsAt;
    private boolean permanent;
}
