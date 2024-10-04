package ci.dgmp.sigomap.authmodule.controller.resources;

import jakarta.validation.Valid;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IActionIdentifierService;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IJwtService;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IUserService;
import ci.dgmp.sigomap.authmodule.model.constants.AuthActions;
import ci.dgmp.sigomap.authmodule.model.dtos.appuser.*;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;
import ci.dgmp.sigomap.modulelog.model.dtos.response.JwtInfos;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@RequiredArgsConstructor
@RestController @RequestMapping("/users") @ResponseStatus(HttpStatus.OK)
public class UserResource
{
    private final IUserService userService;
    private final IJwtService jwtService;
    private final IActionIdentifierService ais;

    @GetMapping(path = "/infos/{userId}")
    public ReadUserDTO getUserInfos(@PathVariable Long userId) throws UnknownHostException {
        return userService.getUserInfos(userId);
    }

    @PostMapping(path = "/open/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginDTO dto) throws UnknownHostException {
        return userService.login(dto);
    }

    @GetMapping(path = "/refresh-token")
    public AuthResponseDTO refreshToken() throws UnknownHostException {
        return userService.refreshToken();
    }

    @GetMapping(path = "/logout")
    public void logout() throws UnknownHostException {
        userService.logout();
    }

    @PostMapping(path = "/create")
    public ReadUserDTO createUser(@RequestBody @Valid CreateUserDTO dto) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.CREATE_USER);
        return userService.createUser(dto, ai);
    }



    @PostMapping(path = "/create-user-and-functions")
    public ReadUserDTO createUserAndFunction(@RequestBody @Valid CreateUserAndFunctionsDTO dto) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.CREATE_USER_AND_FUNCTIONS);
        return userService.createUserAndFunction(dto, ai);
    }

    @PutMapping(path = "/update-user-and-functions")
    public ReadUserDTO updateUserAndFunction(@RequestBody @Valid UpdateUserAndFncDTO dto) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.UPDATE_USER_AND_FUNCTIONS);
        return userService.updateUserAndFunction(dto, ai);
    }

    @GetMapping(path = "/get-update-user-and-fncs-dto/{userId}")
    public UpdateUserAndFncDTO updateUserAndFunction(@PathVariable Long userId) throws UnknownHostException, IllegalAccessException {
        return userService.getUpdateUserAndFunction(userId);
    }

    @GetMapping(path = "/search")
    public Page<ReadUserDTO> searchUser(@RequestParam(defaultValue = "") String key,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) throws UnknownHostException, IllegalAccessException {
        return userService.searchUsers(key, PageRequest.of(page, size));
    }

    @PutMapping(path = "/open/activate-account")
    public ReadUserDTO activateUserAccount(@RequestBody @Valid ActivateAccountDTO dto) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.ACTIVATE_USER_ACCOUNT);
        return userService.activateAccount(dto, ai);
    }

    @PutMapping(path = "/update")
    public ReadUserDTO updateUser(@RequestBody @Valid UpdateUserDTO dto) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.UPDATE_USER);
        return userService.updateUser(dto, ai);
    }

    @PutMapping(path = "/change-password")
    public ReadUserDTO changePassword(@RequestBody @Valid ChangePasswordDTO dto) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.CHANGE_PASSWORD);
        return userService.changePassword(dto, ai);
    }

    @PutMapping(path = "/block/{userId}")
    public boolean blockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.LOCK_USER_ACCOUNT);
        userService.blockAccount(userId, ai);
        return true;
    }

    @PutMapping(path = "/unblock/{userId}")
    public boolean unblockAccount(@PathVariable @Valid long userId ) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.UNLOCK_USER_ACCOUNT);
        userService.unBlockAccount(userId, ai);
        return true;
    }

    @PutMapping(path = "/open/reinit-password")
    public ReadUserDTO reinitPassword(@RequestBody @Valid ReinitialisePasswordDTO dto ) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.REINIT_PASSWORD);
        return userService.reinitialisePassword(dto, ai);
    }

    @PutMapping(path = "/open/send-reinit-password-email/{email}")
    public boolean sendReinitPasswordEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.SEND_REINIT_PASSWORD_EMAIL);
        userService.sendReinitialisePasswordEmail(email, ai);
        return true;
    }

    @PutMapping(path = "/send-activation-email/{email}")
    public boolean sendActivationEmail(@PathVariable @Valid String email ) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.SEND_ACCOUNT_ACTIVATION_EMAIL);
        userService.sendAccountActivationEmail(email, ai);
        return true;
    }

    @GetMapping(path = "/open/click-link/{token}")
    public boolean clickLink(@PathVariable @Valid String token ) throws UnknownHostException, IllegalAccessException {
        ActionIdentifier ai = ais.getActionIdentifierFromSecurityContext(AuthActions.CLICK_LINK);
        userService.clickLink(token, ai);
        return true;
    }

    @GetMapping(path = "/token-introspection")
    public JwtInfos getJwtInfos() throws UnknownHostException, IllegalAccessException {
        return jwtService.getJwtInfos();
    }

    @GetMapping(path = "/open/exists-by-email/{email}/{userId}")
    public boolean existsByEmail(@PathVariable String email, @PathVariable(required = false) Long userId) throws UnknownHostException, IllegalAccessException {
        return userService.existsByEmail(email, userId);
    }

    @GetMapping(path = "/open/exists-by-email/{email}")
    public boolean existsByEmail(@PathVariable String email) throws UnknownHostException, IllegalAccessException {
        return userService.existsByEmail(email, null);
    }

    @GetMapping(path = "/open/exists-by-tel/{tel}/{userId}")
    public boolean existsByTel(@PathVariable String tel, @PathVariable(required = false) Long userId) throws UnknownHostException, IllegalAccessException {
        return userService.existsByTel(tel, userId);
    }

    @GetMapping(path = "/open/exists-by-tel/{tel}")
    public boolean existsByTel(@PathVariable String tel) throws UnknownHostException, IllegalAccessException {
        return userService.existsByTel(tel, null);
    }
}
