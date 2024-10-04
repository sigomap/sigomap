package ci.dgmp.sigomap.authmodule.controller.services.spec;

import ci.dgmp.sigomap.authmodule.model.dtos.appuser.*;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;
import ci.dgmp.sigomap.authmodule.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;

public interface IUserService
{
    AuthResponseDTO login(LoginDTO dto) throws UnknownHostException;
    AuthResponseDTO refreshToken() throws UnknownHostException;
    void logout() throws UnknownHostException;
    ReadUserDTO createUser(CreateUserDTO dto, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException;
    ReadUserDTO updateUser(UpdateUserDTO dto, ActionIdentifier ai) throws UnknownHostException;
    ReadUserDTO changePassword(ChangePasswordDTO dto, ActionIdentifier ai) throws UnknownHostException;
    void blockAccount(Long userId, ActionIdentifier ai) throws UnknownHostException;
    void unBlockAccount(Long userId, ActionIdentifier ai) throws UnknownHostException;
    ReadUserDTO activateAccount(ActivateAccountDTO dto, ActionIdentifier ai) throws UnknownHostException;

    ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto, ActionIdentifier ai) throws UnknownHostException;

    ReadUserDTO findByUsername(String username);

    ReadUserDTO findByEmail(String email);

    ReadUserDTO findByTel(String tel);

    void sendAccountActivationEmail(String email, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException;
    void sendAccountActivationEmail(Long userId, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException;

    //void resendAccountActivationEmail(String username, String email) throws IllegalAccessException;

    void sendReinitialisePasswordEmail(String email, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException;

    void clickLink(String toke, ActionIdentifier ain) throws UnknownHostException;

    UserStatus getUserStatus(Long userId);

    Page<ReadUserDTO> searchUsers(String key, Pageable pageable);

    ReadUserDTO createUserAndFunction(CreateUserAndFunctionsDTO dto, ActionIdentifier ai) throws UnknownHostException, IllegalAccessException;
    ReadUserDTO updateUserAndFunction(UpdateUserAndFncDTO dto, ActionIdentifier ai) throws UnknownHostException;

    ReadUserDTO getUserInfos(Long userId);

    boolean existsByEmail(String email, Long userId);
    boolean existsByTel(String email, Long userId);


    UpdateUserAndFncDTO getUpdateUserAndFunction(Long userId);
}
