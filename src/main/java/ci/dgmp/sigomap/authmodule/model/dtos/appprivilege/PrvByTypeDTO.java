package ci.dgmp.sigomap.authmodule.model.dtos.appprivilege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PrvByTypeDTO
{
    private Long typeId;
    private String typeName;
    private String typeUniqueCode;
    private Set<ReadPrvDTO> privileges;

    public PrvByTypeDTO(String typeName, String typeUniqueCode)
    {
        this.typeName = typeName;
        this.typeUniqueCode = typeUniqueCode;
    }
}
