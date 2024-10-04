package ci.dgmp.sigomap.structuremodule.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;
import ci.dgmp.sigomap.authmodule.model.entities.AppUser;
import ci.dgmp.sigomap.sharedmodule.enums.PersStatus;
import ci.dgmp.sigomap.typemodule.model.entities.Type;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "str")
@Audited @EntityListeners(AuditingEntityListener.class)
public class Structure
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strId;
    @Column(unique = true)
    private String strCode;
    private String strName;
    private long strLevel;
    private String strSigle;
    @ManyToOne @JoinColumn(name = "PARENT_ID")
    private Structure strParent;
    @ManyToOne @JoinColumn(name="ID_TYPE_UA")
    private Type strType;

    private String strTel;
    private String strAddress;
    private String situationGeo;
    private String creationActFilePath;

    @Transient
    private List<Structure> strChildren;
    @Transient
    private MultipartFile creationActFile;


    private String ficheTechPath;
    @Enumerated(EnumType.STRING)
    private PersStatus status;

    @Transient
    private List<AppUser> personnel;

    public Structure(Long strId)
    {
        this.strId = strId;
    }

    @Override
    public String toString()
    {
        return this.strName + " ("+this.strSigle + ")";
    }
}
