package ci.dgmp.sigomap.authmodule.model.dtos.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PrvToMenuDTO
{
    @NotNull(message = "Le code de menu ne peut être null")
    @NotBlank(message = "Le code de menu ne peut être null")
    @ExistingMenuCode
    private String menuCode;
    private String[] prvCodes;
}
