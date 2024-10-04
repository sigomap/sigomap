package ci.dgmp.sigomap.structuremodule.model.dtos;

import ci.dgmp.sigomap.typemodule.model.dtos.ExistingTypeCode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor //@Entity
@CompatibleTypeAndParentStr
public class CreateStrDTO
{
    @Size(message = "Le nom de la structure doit contenir au moins 3 caractères", min = 3)
    @NotNull(message = "Le nom de la structure ne peut être nul")
    private String strName;
    @NotNull(message = "Le sigle de la structure ne peut être nul")
    private String strSigle;
    @NotNull(message = "Le type de la structure ne peut être nul")
    @ExistingTypeCode
    private String typeCode;
    @ExistingOrNullStrId
    private Long parentId;

    private String strTel;
    private String strAddress;
    @NotNull(message = "La situation géographique ne peut être nulle")
    private String situationGeo;
    private MultipartFile creationActFile;

    @Override
    public String toString()
    {
        return this.strName + " ("+this.strSigle + ")";
    }

}
