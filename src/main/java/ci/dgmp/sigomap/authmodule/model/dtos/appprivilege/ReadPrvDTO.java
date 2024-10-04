package ci.dgmp.sigomap.authmodule.model.dtos.appprivilege;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadPrvDTO
{
    private String privilegeCode;
    private String privilegeName;
    private String prvTypeName;
    private String typeCode;
    private Long clientId;
    private String clientName;

    public ReadPrvDTO(String privilegeCode, String privilegeName, String prvTypeName) {
        this.privilegeCode = privilegeCode;
        this.privilegeName = privilegeName;
        this.prvTypeName = prvTypeName;
    }
}
