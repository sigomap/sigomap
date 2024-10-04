package ci.dgmp.sigomap.authmodule.model.dtos.appfunction;

import com.fasterxml.jackson.annotation.JsonFormat;
import ci.dgmp.sigomap.authmodule.model.dtos.appuser.ExistingUserId;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.CoherentDates;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@CoherentDates @UniqueFunctionName
public class UpdateFncDTO
{
    @ExistingFncId
    private Long fncId;
    private Long assoId;
    private Long sectionId;
    private String typeCode;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startsAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endsAt;
    private Set<String> roleCodes = new HashSet<>();

    public UpdateFncDTO(Long fncId, String typeCode, String name, LocalDate startsAt, LocalDate endsAt) {
        this.fncId = fncId;
        this.typeCode = typeCode;
        this.name = name;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }
}