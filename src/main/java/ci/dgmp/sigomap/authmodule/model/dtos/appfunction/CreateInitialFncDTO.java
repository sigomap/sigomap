package ci.dgmp.sigomap.authmodule.model.dtos.appfunction;

import com.fasterxml.jackson.annotation.JsonFormat;
import ci.dgmp.sigomap.authmodule.model.dtos.appuser.ExistingUserId;
import ci.dgmp.sigomap.typemodule.model.dtos.ExistingTypeCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateInitialFncDTO
{
    private String name;
    private Long assoId;
    private Long sectionId;
    //@ExistingUserId
    private Long userId;
    @ExistingTypeCode
    private String typeCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected LocalDate startsAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    protected LocalDate endsAt;
    private Set<String> roleCodes;
}
