package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidToken(message = "passwordReinitialisationToken::Lien invalide") @ConcordantPassword
@NotIdentiqueNewAndOldPassword
public class ReinitialisePasswordDTO
{
    @ExistingEmail
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=/\\*\\-_<>?!:,;])(?=.*[a-zA-Z\\d@#$%^&+=/\\*\\-_<>?!:,;]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, des minuscules, des majuscules, des chiffres et des caractères spéciaux.")
    private String newPassword;
    private String confirmNewPassword;
    @ValidToken @NoneExpiredToken @NoneAlreadyUsedToken
    private String passwordReinitialisationToken;
}
