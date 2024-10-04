package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ConcordantUserIdAndEmail @ConcordantPassword @NotIdentiqueNewAndOldPassword
public class ChangePasswordDTO
{
    @ExistingUserId
    private Long userId;
    private String email;

    private String oldPassword;
    @NotBlank(message = "Le mot de passe ne peut être nul")
    @Size(message = "Le mot de passe doit contenir au moins 4 caractères", min = 4)
    @NotNull(message = "Le mot de passe ne peut être nul")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=/\\*\\-_<>?!:,;])(?=.*[a-zA-Z\\d@#$%^&+=/\\*\\-_<>?!:,;]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, des minuscules, des majuscules, des chiffres et des caractères spéciaux.")
    private String newPassword;
    private String confirmPassword;
}
