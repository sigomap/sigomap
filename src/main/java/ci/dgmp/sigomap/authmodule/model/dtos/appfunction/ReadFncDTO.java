package ci.dgmp.sigomap.authmodule.model.dtos.appfunction;

import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.approle.ReadRoleDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadFncDTO
{
    private Long id;
    private Long visibilityId;
    private String name;
    private Long userId;
    private String email;
    protected int fncStatus;// 1 == actif, 2 == inactif, 3 == revoke
    protected LocalDate startsAt;
    protected LocalDate endsAt;
    private List<ReadRoleDTO> roles;
    private List<ReadPrvDTO> privileges;
    private Set<String> menus;
}
