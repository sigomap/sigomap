package ci.dgmp.sigomap.authmodule.model.dtos.approle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateRoleDTO
{
    @UniqueRoleCode @NotNull(message = "Le code du rôle ne peut être nul")  @NotBlank(message = "Le code du rôle ne peut être nul")
    private String roleCode;
    @UniqueRoleName @NotNull(message = "Le nom du rôle ne peut être nul")  @NotBlank(message = "Le nom du rôle ne peut être nul")
    private String roleName;
    @NotEmpty(message = "La liste des privilèges ne peut être vide")
    private Set<String> prvCodes;
}
