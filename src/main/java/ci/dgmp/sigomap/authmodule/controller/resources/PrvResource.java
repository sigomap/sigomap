package ci.dgmp.sigomap.authmodule.controller.resources;

import jakarta.validation.Valid;
import ci.dgmp.sigomap.authmodule.controller.repositories.PrvRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.PrvToRoleAssRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IPrivilegeService;
import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.CreatePrivilegeDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.PrvByTypeDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.UpdatePrivilegeDTO;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController @RequestMapping("/privileges") @ResponseStatus(HttpStatus.OK)
public class PrvResource
{
    private final IPrivilegeService prvService;
    private final PrvRepo prvRepo;
    private final PrvToRoleAssRepo ptrRepo;

    @PostMapping(path = "/create")
    public ReadPrvDTO createPrv(@RequestBody @Valid CreatePrivilegeDTO dto) throws UnknownHostException, IllegalAccessException {
        return prvService.createPrivilege(dto);
    }

    @PutMapping(path = "/update")
    public ReadPrvDTO updatePrv(@RequestBody @Valid UpdatePrivilegeDTO dto) throws UnknownHostException, IllegalAccessException {
        return prvService.updatePrivilege(dto);
    }

    @GetMapping(path = "/search")
    public Page<ReadPrvDTO> searchPrv(@RequestParam(defaultValue = "") String key, @RequestParam List<String> typePrvUniqueCodes, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10000") int size){
        return prvService.searchPrivileges(key, typePrvUniqueCodes, PageRequest.of(page, size));
    }

    @GetMapping(path = "/grouped-by-type")
    public List<PrvByTypeDTO> getAllPrivilegesGroupedByType(){
        return prvService.getAllPrivlegesGroupesByType();
    }

    @GetMapping(path = "/existsByName/{name}")
    public boolean existsByName(@PathVariable String name, @RequestParam(required = false, defaultValue = "") String privilegeCode){
        return prvService.existsByName(name, privilegeCode);
    }

    @GetMapping(path = "/existsByCode/{privilegeCode}")
    public boolean existsByCode(@PathVariable String privilegeCode) throws UnknownHostException, IllegalAccessException {
        return prvRepo.existsByCode(privilegeCode);
    }

    @GetMapping(path = "/privilegeCodes-for-roleCodes")
    public Set<String> getPrvCodesForRoleCodes(@RequestParam Set<String> roleCodes) throws UnknownHostException, IllegalAccessException {
        return ptrRepo.findActivePrvCodesForRoles(roleCodes);
    }

    @GetMapping(path = "/privileges-for-roleCodes")
    public Set<ReadPrvDTO> getPrvsForRoleCodes(@RequestParam Set<String> roleCodes) throws UnknownHostException, IllegalAccessException {
        return ptrRepo.findActivePrivilegesForRoles(roleCodes);
    }

    @GetMapping(path = "/privilege-belong-to-any-role")
    public boolean prvBelongToAnyRole(@RequestParam Set<String> roleCodes, @RequestParam String prvCode) throws UnknownHostException, IllegalAccessException {
        return ptrRepo.prvBelongToAnyRole(prvCode, roleCodes);
    }

    @GetMapping(path = "/types")
    public List<SelectOption> getPrvTypesOptions(){
        return prvService.getPrivilegeTypes();
    }

    @GetMapping(path = "/type-is-valid/{typeCode}")
    public boolean privilegeTypeIsValid(@PathVariable String typeCode)
    {
        return prvService.privilegeTypeIsValid(typeCode);
    }

    @GetMapping(path = "/by-typeCodes")
    public List<SelectOption> getPrivilegesByTypes(@RequestParam(required = false) List<String> typeCodes)
    {
        return prvService.getPrivilegesByTypes(typeCodes);
    }

    @GetMapping(path = "/all-options")
    public List<SelectOption> getPrivilegeOptions(@RequestParam(required = false) List<String> typeCodes)
    {
        return prvService.getAllPrivilgesOptions();
    }

    @GetMapping(path = "/options-by-role-codes")
    public List<SelectOption> getPrivilegeOptions(@RequestParam(required = false) Set<String> roleCodes)
    {
        return prvService.getPrivilegesOptionsByRoleCodes(roleCodes);
    }
}
