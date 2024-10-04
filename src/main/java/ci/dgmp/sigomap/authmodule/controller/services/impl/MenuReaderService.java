package ci.dgmp.sigomap.authmodule.controller.services.impl;

import ci.dgmp.sigomap.authmodule.controller.repositories.MenuRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.PrvRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IMenuReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class MenuReaderService implements IMenuReaderService
{
    private final MenuRepo menuRepo;
    private final PrvRepo prvRepo;

    @Override
    public boolean menuHasPrv(String menuCode, String prvCode)
    {
        return menuRepo.menuHasPrivilege(menuCode, prvCode);
    }

    @Override
    public boolean prvCanSeeMenu(String prvCode, String menuCode) {
        return menuRepo.menuHasPrivilege(menuCode, prvCode);
    }

    @Override
    public boolean fncCanSeeMenu(Long fncId, String menuCode) {
        Set<String> fncPrvCodes = prvRepo.getFunctionPrvCodes(fncId);
        Set<String> menuPrvCodes = this.getMenuPrvCodes(menuCode);
        if(fncPrvCodes == null || menuPrvCodes == null) return false;
        fncPrvCodes.retainAll(menuPrvCodes);
        return !fncPrvCodes.isEmpty();
    }

    @Override
    public Set<String> getMenuPrvCodes(String menuCode) {
        String prvCodeChain = menuRepo.getPrvsCodesByMenuCode(menuCode);
        return  prvCodeChain == null ? new HashSet<>() : new HashSet<>(Arrays.asList(prvCodeChain.split(",")));
    }
    @Override
    public Set<String> getMenusByFncId(Long fncId)
    {
        Set<String> prvCodes = prvRepo.getFunctionPrvCodes(fncId);
        Set<String> menus = menuRepo.findAll().stream()
                .filter(m->m.getPrvsCodes().stream().anyMatch(menuCode->prvCodes.contains(menuCode)))
                .map(m->"MENU_" + m.getMenuCode())
                .collect(Collectors.toSet());
        return menus;
    }
}
