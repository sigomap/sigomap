package ci.dgmp.sigomap.modulelog.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class JwtInfos
{
    private Long userId;
    private String userEmail;
    private List<String> authorities;
    private Long fncId;
    private String fncName;
    private LocalDate fncStartingDate;
    private LocalDate fncEndingDate;
    private Date tokenStartingDate;
    private Date tokenEndingDate;
    private String connectionId;
    private Long strId;
    private String strCode;
    private String strName;
    private String strSigle;
}
