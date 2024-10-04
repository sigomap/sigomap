package ci.dgmp.sigomap.structuremodule.model.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateStrDTO
{
    @ExistingStrId
    private Long strId;

    @Size(message = "Le nom de la structure doit contenir au moins 3 caractères", min = 3)
    @NotNull(message = "Le nom de la structure ne peut être nul")
    private String strName;
    @NotNull(message = "Le sigle de la structure ne peut être nul")
    private String strSigle;

    private String strTel;
    private String strAddress;
    @NotNull(message = "La situation géographique ne peut être nulle")
    private String situationGeo;
    private MultipartFile creationActFile;
}
