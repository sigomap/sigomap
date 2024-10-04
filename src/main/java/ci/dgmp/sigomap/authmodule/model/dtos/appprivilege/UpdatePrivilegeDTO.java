package ci.dgmp.sigomap.authmodule.model.dtos.appprivilege;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UpdatePrivilegeDTO
{
    @ExistingPrivilegeCode
    private String privilegeCode;
    @UniquePrvName
    private String privilegeName;
    @ValidPrvType
    private String typeCode;
}