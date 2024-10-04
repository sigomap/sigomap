package ci.dgmp.sigomap.authmodule.controller.resources;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import ci.dgmp.sigomap.authmodule.controller.repositories.FunctionRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IActionIdentifierService;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IFunctionService;
import ci.dgmp.sigomap.authmodule.model.constants.AuthActions;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.CreateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.FncMapper;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.ReadFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appuser.AuthResponseDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.SetAuthoritiesToFunctionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;

import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequiredArgsConstructor
@RequestMapping(path = "/functions") @ResponseStatus(HttpStatus.OK)
public class FunctionResource
{
    private final IFunctionService functionService;
    private final FunctionRepo functionRepo;
    private final FncMapper fncMapper;
    private final IActionIdentifierService ais;

    @PostMapping(path = "/create")
    public ReadFncDTO createFunction(@RequestBody @Valid CreateFncDTO dto) throws UnknownHostException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.CREATE_FNC);
        return functionService.createFnc(dto, ai);
    }

    @PutMapping(path = "/update")
    public ReadFncDTO updateFunction(@RequestBody @Valid  UpdateFncDTO dto) throws UnknownHostException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.UPDATE_FUNCTION);
        return functionService.updateFunction(dto, ai);
    }

    @GetMapping(path = "/infos/{foncId}")
    public ReadFncDTO getFoncInfos(@PathVariable Long foncId) throws UnknownHostException
    {
        return functionService.getFunctioninfos(foncId);
    }

    @GetMapping(path = "/get-current-fncId-for-user/{userId}")
    public Long getActiveCurrentFunctionId(@PathVariable Long userId)
    {
        return functionService.getActiveCurrentFunctionId(userId);
    }

    @GetMapping(path = "/get-current-fnc-for-user/{userId}")
    public ReadFncDTO getActiveCurrentFunction(@PathVariable Long userId)
    {
        return functionService.getActiveCurrentFunction(userId);
    }

    @PutMapping(path = "/set-fnc-as-default/{fncId}")
    public AuthResponseDTO setFunctionAsDefault(@PathVariable Long fncId)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.SET_FNC_AS_DEFAULT);
        return functionService.setFunctionAsDefault(fncId, ai);
    }
    @PutMapping(path = "/revoke/{fncId}")
    public void revokeFunction(@PathVariable Long fncId)
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.REVOKE_FNC);
        functionService.revokeFunction(fncId, ai);
    }
    @PutMapping(path = "/restore/{fncId}")
    public void restoreFunction(@PathVariable Long fncId) throws UnknownHostException
    {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.RESTORE_FNC);
        functionService.restoreFunction(fncId, ai);
    }

    @GetMapping(path = "/active-fnc-for-user/{userId}")
    public List<ReadFncDTO> getActiveFunctionsForUser(@PathVariable Long userId)
    {
        return functionRepo.findActiveByUser(userId).stream().map(fncMapper::mapToReadFncDto).sorted(Comparator.comparingInt(ReadFncDTO::getFncStatus)).collect(Collectors.toList());
    }

    @GetMapping(path = "/all-fnc-for-user/{userId}")
    public List<ReadFncDTO> getUsersAllFunctions(@PathVariable Long userId)
    {
        return functionRepo.findAllByUser(userId).stream().map(fncMapper::mapToReadFncDto).collect(Collectors.toList());
    }

    @GetMapping(path = "/search/{userId}")
    public Page<ReadFncDTO> searchUsersFunctions(@PathVariable Long userId,
                                                 @RequestParam(defaultValue = "", required = false) String key,
                                                 @RequestParam(defaultValue = "0", required = false) int page,
                                                 @RequestParam(defaultValue = "5", required = false) int size,
                                                 @RequestParam(defaultValue = "false") boolean withRevoked)
    {
        return functionService.search(userId, key, page, size, withRevoked); //functionRepo.findAllByUser(userId).stream().map(fncMapper::mapToReadFncDto).collect(Collectors.toList());
    }

    @GetMapping(path = "/get-update-fnc-dto/{fncId}")
    public UpdateFncDTO getUpdateFncDTO(@PathVariable Long fncId)
    {
        return functionRepo.getUpdateFncDTO(fncId);
    }
}