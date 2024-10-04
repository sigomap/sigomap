package ci.dgmp.sigomap.authmodule.model.dtos.appuser;

import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.ReadFncDTO;
import ci.dgmp.sigomap.authmodule.model.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReadUserDTO
{
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String tel;
    private String lieuNaissance;
    private LocalDate dateNaissance;
    private String civilite;
    private String paysCode;
    private String nationalite;
    private String nomPere;
    private String nomMere;

    private boolean active;

    private boolean notBlocked;
    private Long currentFunctionId;

    private ReadFncDTO currentFnc;

    public ReadUserDTO(Long userId, String firstName, String lastName,
                       String email, String tel, String civilite,
                       boolean active, boolean notBlocked)
    {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.civilite = civilite;
        this.active = active;
        this.notBlocked = notBlocked;

    }
}
