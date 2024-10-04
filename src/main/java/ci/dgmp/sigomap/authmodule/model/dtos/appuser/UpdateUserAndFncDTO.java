package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import jakarta.validation.Valid;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateUserAndFncDTO
{
    @Valid
    private UpdateUserDTO updateUserDTO;
    @Valid
    private List<UpdateFncDTO> updateFncDTOS;
}
