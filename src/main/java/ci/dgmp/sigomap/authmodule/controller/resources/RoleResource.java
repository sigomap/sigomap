package ci.dgmp.sigomap.authmodule.controller.resources;

import jakarta.validation.Valid;
import ci.dgmp.sigomap.authmodule.controller.repositories.RoleRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IRoleService;
import ci.dgmp.sigomap.authmodule.model.dtos.approle.CreateRoleDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.approle.ReadRoleDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.PrvsToRoleDTO;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;

@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
@RestController @RequestMapping("/roles")
public class RoleResource
{
    private final IRoleService roleService;
    private final RoleRepo roleRepo;

    @PostMapping(path = "/create")
    public ReadRoleDTO createRole(@RequestBody @Valid CreateRoleDTO dto) throws UnknownHostException, IllegalAccessException {
        return roleService.createRole(dto);
    }

    @PutMapping(path = "/update")
    public ReadRoleDTO setPrivileges(@RequestBody @Valid PrvsToRoleDTO dto) throws UnknownHostException, IllegalAccessException {
        return roleService.updateRole(dto);
    }

    @GetMapping(path = "/search")
    public Page<ReadRoleDTO> searchrole(@RequestParam(defaultValue = "") String key, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) throws UnknownHostException, IllegalAccessException {
        return roleService.searchRoles(key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/existsByName/{name}")
    public boolean existsByName(@PathVariable String name) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByName(name);
    }

    @GetMapping(path = "/existsByName/{name}/{roleCode}")
    public boolean existsByName(@PathVariable String name, @PathVariable String roleCode) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByName(name, roleCode);
    }

    @GetMapping(path = "/existsByCode/{code}")
    public boolean existsByCode(@PathVariable String code) throws UnknownHostException, IllegalAccessException {
        return roleRepo.existsByCode(code);
    }

    @GetMapping(path = "/all-options")
    public List<SelectOption> getAllOptions() throws UnknownHostException, IllegalAccessException {
        return roleRepo.getAllAsSelectOptions();
    }

    @GetMapping(path = "/options-by-roleCodes")
    public List<SelectOption> getOptionsByRolCodes(@RequestParam List<String> roleCodes) throws UnknownHostException, IllegalAccessException {
        return roleService.getRoleOptions(roleCodes);
    }
}
