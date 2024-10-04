package ci.dgmp.sigomap.authmodule.controller.resources;

import jakarta.validation.Valid;
import ci.dgmp.sigomap.authmodule.controller.repositories.MenuRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IMenuMutatorService;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IMenuReaderService;
import ci.dgmp.sigomap.authmodule.model.dtos.menu.CreateMenuDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.menu.PrvToMenuDTO;
import ci.dgmp.sigomap.authmodule.model.entities.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.Set;

@RequiredArgsConstructor
@RestController @RequestMapping("/menu") @ResponseStatus(HttpStatus.OK)
public class MenuResource
{
    private final IMenuReaderService menmenuReaderService;
    private final IMenuMutatorService menuMutatorService;
    private final MenuRepo menuRepo;

    @PostMapping(path = "/create")
    public Menu createMenu(@RequestBody @Valid CreateMenuDTO dto) throws UnknownHostException, IllegalAccessException {
        return menuMutatorService.createMenu(dto);
    }

    @GetMapping(path = "/by-fncId/{fncId}")
    public Set<String> getMenuByFncId(@PathVariable Long fncId)
    {
        return menmenuReaderService.getMenusByFncId(fncId);
    }

    @GetMapping(path = "/search")
    public Page<Menu> searchMenu(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int num, @RequestParam(defaultValue = "2") int size) throws UnknownHostException, IllegalAccessException {
        return menuRepo.searchMenu2(key, PageRequest.of(num, size));
    }

    @GetMapping(path = "/fnc-can-see-menu/{fncId}/{nemuCode}")
    public boolean FncCanSeeMenu(@PathVariable Long fncId, @PathVariable String nemuCode){
        return menmenuReaderService.fncCanSeeMenu(fncId, nemuCode);
    }

    @GetMapping(path = "/prv-can-see-menu/{prvCode}/{nemuCode}")
    public boolean prvCanSeeMenu(@PathVariable String prvCode, @PathVariable String menuCode){
        return menmenuReaderService.prvCanSeeMenu(prvCode, menuCode);
    }

    @PostMapping(path = "/add-prv-to-menu")
    public void addPrvToMenu(@RequestBody @Valid PrvToMenuDTO dto) throws UnknownHostException, IllegalAccessException {
        menuMutatorService.addPrvToMenu(dto);
    }

    @PostMapping(path = "/rmv-prv-to-menu")
    public void removePrvToMenu(@RequestBody @Valid PrvToMenuDTO dto) throws UnknownHostException, IllegalAccessException {
        menuMutatorService.removePrvToMenu(dto);
    }
}