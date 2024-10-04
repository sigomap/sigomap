package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateActiveUserDTO
{
    private String username;
    private String password;
    private String rePassword;
    private String defaultPassword;
    private String email;
    private String tel;
    private Long strId;
    private String token;
}
