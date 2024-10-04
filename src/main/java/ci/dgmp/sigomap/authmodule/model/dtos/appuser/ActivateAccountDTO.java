package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidToken @ConcordantPassword //@ConcordantUserIdAndEmail
public class ActivateAccountDTO
{
    @NotBlank(message = "L'email ne peut être nul")
    @Email(message = "L'email  n'est pas valide")
    private String email;
    @NotBlank(message = "Le mot de passe ne peut être nul")
    @Size(message = "Le mot de passe doit contenir au moins 4 caractères", min = 4)
    @NotNull(message = "Le mot de passe ne peut être nul")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=/\\*\\-_<>?!:,;])(?=.*[a-zA-Z\\d@#$%^&+=/\\*\\-_<>?!:,;]).{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, des minuscules, des majuscules, des chiffres et des caractères spéciaux.")
    private String password;
    private String confirmPassword;
    @ValidToken @NoneExpiredToken @NoneAlreadyUsedToken
    private String activationToken;


    protected String civiliteCode;
    protected String codePays;
    protected String typePieceCode;
    private Long gradeId;
    private int indiceFonctionnaire;
    protected String numPiece;
    protected String typeUtilisateurCode;
}
/*
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/lenicoulibaly/eecole.git
git push -u origin main
 */