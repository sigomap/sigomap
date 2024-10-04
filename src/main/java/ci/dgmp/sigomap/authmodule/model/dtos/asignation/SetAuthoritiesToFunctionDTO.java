package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SetAuthoritiesToFunctionDTO
{
    @ExistingAssId
    private Long fncId;
    private LocalDate startsAt;
    private LocalDate endsAt;
    private Set<String> roleCodes = new HashSet<>();
}