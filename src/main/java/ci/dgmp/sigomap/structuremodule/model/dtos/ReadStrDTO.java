package ci.dgmp.sigomap.structuremodule.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ci.dgmp.sigomap.structuremodule.model.entities.Structure;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReadStrDTO
{
    private Long strId;
    private String strCode;
    private String strName;
    private String strSigle;

    private String strType;
    private String strTel;
    private String strAddress;
    private String situationGeo;

    private Long parentId;
    private String parentName;
    private String parentSigle;

    private Long respoId;
    private String respoName;
    private String respoMatricule;
    private List<Structure> hierarchy;
    private String hierarchySigles;

    public ReadStrDTO(Long strId, String strName, String strSigle, String hierarchySigles)
    {
        this.strId = strId;
        this.strName = strName;
        this.strSigle = strSigle;
        this.hierarchySigles = hierarchySigles;
    }

    @Override
    public String toString()
    {
        return this.strName + (this.strSigle == null ? "" : " ("+this.strSigle + ")");
    }
}