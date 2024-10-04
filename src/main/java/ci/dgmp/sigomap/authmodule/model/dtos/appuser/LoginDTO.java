package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@UserHasAnyFunction @NotExpiredCredentials
public class LoginDTO
{
    @NotNull(message = "Veuillez saisir votre login")
    private String username;
    @NotNull(message = "Veuillez saisir votre mot de passe")
    private String password;
}
