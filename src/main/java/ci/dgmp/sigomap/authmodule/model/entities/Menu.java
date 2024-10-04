package ci.dgmp.sigomap.authmodule.model.entities;

import ci.dgmp.sigomap.sharedmodule.enums.PersStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
public class Menu
{
    @Id
    private String menuCode;
    @Column(unique = true)
    private String name;
    @Column(length=4000)
    private String prvsCodesChain;
    @Transient
    private List<String> prvsCodes;
    @Enumerated(EnumType.STRING)
    private PersStatus status;
    @Transient
    public static final String chainSeparator = "::";

    public List<String> getPrvsCodes()
    {
        if(this.prvsCodesChain == null) return new ArrayList<>();
        return Arrays.asList(this.prvsCodesChain.split(Menu.chainSeparator));
    }

    public Menu(String menuCode, String name, String prvsCodesChain, PersStatus status) {
        this.menuCode = menuCode;
        this.name = name;
        this.prvsCodesChain = prvsCodesChain;
        this.status = status;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public class MenuResp
    {
        private String menuCode;
        private String name;
    }
}
