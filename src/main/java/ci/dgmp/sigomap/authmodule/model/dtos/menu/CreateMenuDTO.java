package ci.dgmp.sigomap.authmodule.model.dtos.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateMenuDTO
{
    @NotNull(message = "Le code du menu ne peut être null")
    @NotBlank(message = "Le code du menu ne peut être null")
    @UniqueMenuCode
    private String menuCode;
    @NotNull(message = "Le nom du menu ne peut être null")
    @NotBlank(message = "Le nom du menu ne peut être null")
    @UniqueMenuName
    private String name;
    private String[] prvsCodes;
}
