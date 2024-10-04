package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.ExistingFncId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RolesToFncDTO
{
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private Set<Long> roleIds = new HashSet<>();
    @ExistingFncId
    private Long fncId;
}
