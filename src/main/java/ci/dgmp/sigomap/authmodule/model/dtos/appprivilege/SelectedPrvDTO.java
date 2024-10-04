package ci.dgmp.sigomap.authmodule.model.dtos.appprivilege;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SelectedPrvDTO
{
    private String privilegeCode;
    private String privilegeName;
    private boolean selected;
    private boolean owned;
}