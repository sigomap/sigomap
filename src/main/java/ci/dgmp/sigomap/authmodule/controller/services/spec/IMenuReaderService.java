package ci.dgmp.sigomap.authmodule.controller.services.spec;


import java.util.Set;

public interface IMenuReaderService
{
    boolean menuHasPrv(String menuCode, String prvCode);
    boolean prvCanSeeMenu(String prvCode, String menuCode);

    Set<String> getMenuPrvCodes(String menuCode);

    Set<String> getMenusByFncId(Long fncId);

    boolean fncCanSeeMenu(Long fncId, String menuCode);


}
