package ci.dgmp.sigomap.authmodule.model.dtos.approle;

import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadRoleDTO
{
    private String roleCode;
    private String roleName;
    private Set<ReadPrvDTO> privileges;
    private List<SelectOption> privilegeOptions;
}
