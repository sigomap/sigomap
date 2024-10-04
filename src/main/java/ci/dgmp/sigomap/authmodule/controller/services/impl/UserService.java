package ci.dgmp.sigomap.authmodule.controller.services.impl;

import org.springframework.context.ApplicationEventPublisher;
import ci.dgmp.sigomap.authmodule.controller.repositories.AccountTokenRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.FunctionRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.RoleToFunctionAssRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.UserRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.*;
import ci.dgmp.sigomap.authmodule.model.constants.AuthActions;
import ci.dgmp.sigomap.authmodule.model.constants.AuthTables;
import ci.dgmp.sigomap.authmodule.model.constants.SecurityConstants;
import ci.dgmp.sigomap.authmodule.model.constants.SecurityErrorMsg;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.CreateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.CreateInitialFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.ReadFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appuser.*;
import ci.dgmp.sigomap.authmodule.model.entities.AccountToken;
import ci.dgmp.sigomap.authmodule.model.entities.ActionIdentifier;
import ci.dgmp.sigomap.authmodule.model.entities.AppUser;
import ci.dgmp.sigomap.authmodule.model.enums.UserStatus;
import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.modulelog.model.entities.Log;
import ci.dgmp.sigomap.modulestatut.repositories.StatutRepository;
import ci.dgmp.sigomap.notificationmodule.controller.dao.EmailNotificationRepo;
import ci.dgmp.sigomap.notificationmodule.controller.services.EmailSenderService;
import ci.dgmp.sigomap.notificationmodule.controller.services.EmailServiceConfig;
import ci.dgmp.sigomap.notificationmodule.model.entities.EmailNotification;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.ObjectCopier;
import ci.dgmp.sigomap.sharedmodule.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class UserService implements IUserService
{
    private final UserRepo userRepo;
    private final IAccountTokenService accountTokenService;
    private final UserMapper userMapper;
    private final IJwtService jwtService;
    private final AccountTokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final EmailServiceConfig emailServiceConfig;
    private final EmailNotificationRepo emailRepo;
    private final ILogService logger;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService uds;
    private final ObjectCopier<AppUser> userCopier;
    private final StatutRepository staRepo;
    private final FunctionRepo fncRepo;
    private final IFunctionService functionService;
    private final RoleToFunctionAssRepo rtfRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Override @Transactional
    public AuthResponseDTO login(LoginDTO dto) throws UnknownHostException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        UserDetails userDetails = uds.loadUserByUsername(dto.getUsername());
        Log log = logger.logLoginOrLogout(dto.getUsername(), AuthActions.LOGIN);
        return jwtService.generateJwt(userDetails, log.getConnectionId());
    }
    @Override
    public AuthResponseDTO refreshToken() throws UnknownHostException {
        String username = jwtService.extractUsername(); String conId = jwtService.extractConnectionId();
        UserDetails userDetails = uds.loadUserByUsername(username);
        Log log = logger.logLoginOrLogout(username, AuthActions.REFRESH_TOKEN, conId);
        return jwtService.generateJwt(userDetails, log.getConnectionId());
    }
    @Override
    public void logout() throws UnknownHostException
    {
        String username = jwtService.extractUsername();
        logger.logLoginOrLogout(username, AuthActions.LOGOUT);
    }

    @Override @Transactional
    public ReadUserDTO createUser(CreateUserDTO dto, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException {
        Long actorUserId = userRepo.getUserIdByEmail(jwtService.extractUsername());

        AppUser user = userMapper.mapToUser(dto);
        user = userRepo.save(user);
        BeanUtils.copyProperties(ai, user);

        AccountToken accountToken = new AccountToken(UUID.randomUUID().toString(), user);
        accountToken = tokenRepo.save(accountToken);
        BeanUtils.copyProperties(ai, accountToken);

        EmailNotification emailNotification = new EmailNotification(user, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), actorUserId);
        emailSenderService.sendAccountActivationEmail(user.getEmail(), user.getFirstName(), emailServiceConfig.getActivateAccountLink() + "/" + accountToken.getToken());

        emailNotification.setSent(true);
        emailNotification = emailRepo.save(emailNotification);
        BeanUtils.copyProperties(ai, emailNotification);
        accountToken.setEmailSent(true);

        return userMapper.mapToReadUserDTO(user);
    }

    @Override @Transactional
    public ReadUserDTO activateAccount(ActivateAccountDTO dto, ActionIdentifier ai) throws UnknownHostException
    {
        AppUser user = userRepo.findByEmail(dto.getEmail()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        AppUser oldUser = new AppUser();BeanUtils.copyProperties(user, oldUser);
        user.setActive(true);
        user.setNotBlocked(true);
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setChangePasswordDate(LocalDateTime.now());
        user.setUpdatedBy(dto.getEmail());
        BeanUtils.copyProperties(ai, user);

        AccountToken token = tokenRepo.findByToken(dto.getActivationToken()).orElseThrow(()->new AppException(SecurityErrorMsg.INVALID_ACTIVATION_TOKEN_ERROR_MSG));
        token.setAlreadyUsed(true);
        token.setUpdatedBy(dto.getEmail());
        BeanUtils.copyProperties(ai, token);
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO updateUser(UpdateUserDTO dto, ActionIdentifier ai) throws UnknownHostException {
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        BeanUtils.copyProperties(dto, user);
        user = userRepo.save(user);
        BeanUtils.copyProperties(ai, user);
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO changePassword(ChangePasswordDTO dto, ActionIdentifier ai) throws UnknownHostException
    {
        AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));

        if(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) throw new AppException("L'ancien mot de passe est incorrect");
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setChangePasswordDate(LocalDateTime.now());
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        return readUserDTO;
    }

    @Override @Transactional
    public ReadUserDTO reinitialisePassword(ReinitialisePasswordDTO dto, ActionIdentifier ai) throws UnknownHostException {
        AppUser user = userRepo.findByEmail(dto.getEmail()).orElseThrow(()->new AppException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        AppUser oldUser = userCopier.copy(user);
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setChangePasswordDate(LocalDateTime.now());
        Log mainLog = logger.loggOffConnection(AuthActions.REINIT_PASSWORD, dto.getEmail(),oldUser, user, AuthTables.USER_TABLE, null);
        AccountToken token = tokenRepo.findByToken(dto.getPasswordReinitialisationToken()).orElseThrow(()->new AppException(SecurityErrorMsg.INVALID_ACTIVATION_TOKEN_ERROR_MSG));

        AccountToken oldToken = new AccountToken();BeanUtils.copyProperties(token, oldToken);
        token.setUsageDate(LocalDateTime.now());
        token.setAlreadyUsed(true);
        logger.loggOffConnection(AuthActions.SET_TOKEN_AS_ALREADY_USED, dto.getEmail(),oldToken, token, AuthTables.ACCOUNT_TOKEN, mainLog.getId());
        ReadUserDTO readUserDTO = userMapper.mapToReadUserDTO(user);
        return readUserDTO;
    }

    @Override @Transactional
    public void blockAccount(Long userId, ActionIdentifier ai) throws UnknownHostException {
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return;
        if(!user.isNotBlocked()) return;
        user.setNotBlocked(false);
        BeanUtils.copyProperties(ai, user);
    }

    @Override @Transactional
    public void unBlockAccount(Long userId, ActionIdentifier ai) throws UnknownHostException {
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return;
        if(user.isNotBlocked()) return;
        user.setNotBlocked(true);
        BeanUtils.copyProperties(ai, user);
    }

    @Override
    public ReadUserDTO findByUsername(String username)
    {
        AppUser user = userRepo.findByEmail(username).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByEmail(String email)
    {
        AppUser user = userRepo.findByEmail(email).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override
    public ReadUserDTO findByTel(String tel)
    {
        AppUser user = userRepo.findByTel(tel).orElse(null);
        return user == null ? null : userMapper.mapToReadUserDTO(user);
    }

    @Override @Transactional
    public void sendAccountActivationEmail(String email, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException {
        boolean isNotLoggedIn = jwtService.getCurrentJwt() == null;
        AppUser loadedUser = this.userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        String username = loadedUser.getFirstName();
        //if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}
        if(loadedUser.isActive() && loadedUser.isNotBlocked()) {throw new AppException(SecurityErrorMsg.ALREADY_ACTIVATED_ACCOUNT_ERROR_MSG);}
        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser, ai);

        Long actorUserId = userRepo.getUserIdByEmail(isNotLoggedIn ? loadedUser.getEmail() : jwtService.extractUsername());
        EmailNotification emailNotification = new EmailNotification(loadedUser, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), actorUserId);
        emailSenderService.sendAccountActivationEmail(email, username, emailServiceConfig.getActivateAccountLink() + "/" + accountToken.getToken());
        emailNotification.setSent(true);
        accountToken.setEmailSent(true);
        BeanUtils.copyProperties(ai, emailNotification);
        emailNotification = emailRepo.save(emailNotification);
    }

    @Override @Transactional
    public void sendAccountActivationEmail(Long userId, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException {
        AppUser loadedUser = this.userRepo.findById(userId).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USER_ID_NOT_FOUND_ERROR_MSG));
        this.sendAccountActivationEmail(loadedUser.getEmail(), ai);
    }

    @Override @Transactional
    public void sendReinitialisePasswordEmail(String email, ActionIdentifier ai) throws IllegalAccessException, UnknownHostException
    {
        AppUser loadedUser = this.userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(SecurityErrorMsg.USERNAME_NOT_FOUND_ERROR_MSG));
        String username = loadedUser.getFirstName();
        if(!loadedUser.getEmail().equals(email)) {throw new AppException(SecurityErrorMsg.INVALID_USERNAME_OR_EMAIL_ERROR_MSG);}
        AccountToken accountToken = accountTokenService.createAccountToken(loadedUser, ai);

        EmailNotification emailNotification = new EmailNotification(loadedUser, SecurityConstants.ACCOUNT_ACTIVATION_REQUEST_OBJECT, accountToken.getToken(), loadedUser.getUserId());
        emailSenderService.sendReinitialisePasswordEmail(email, username, emailServiceConfig.getReinitPasswordLink() + "/" + accountToken.getToken());
        emailNotification.setSent(true);
        accountToken.setEmailSent(true);
        BeanUtils.copyProperties(ai, emailNotification);
        emailRepo.save(emailNotification);
    }

    @Override @Transactional
    public void clickLink(String token, ActionIdentifier ai) throws UnknownHostException
    {
        Optional<AccountToken> accountToken$ = tokenRepo.findByToken(token);
        if(!accountToken$.isPresent()) return; //tCohrow new AppException(UNKNOWN_ACCOUNT_TOKEN_ERROR_MSG);
        EmailNotification notification = emailRepo.findByToken(accountToken$.get().getToken()).orElse(null);
        EmailNotification oldNotification = new EmailNotification();BeanUtils.copyProperties(notification, oldNotification);
        if(notification != null)
        {
            notification.setSeen(true);
            notification.setSent(true);
            logger.loggOffConnection(AuthActions.CLICK_LINK, notification.getEmail(), oldNotification, notification, AuthTables.EMAIL_NOTIFICATION, null);
        }
    }

    @Override
    public UserStatus getUserStatus(Long userId)
    {
        AppUser user = userRepo.findById(userId).orElse(null);
        if(user == null) return  UserStatus.UN_KNOWN;
        if(user.isActive() && user.isNotBlocked()) return UserStatus.ACTIVE;
        if(user.isActive() && !user.isNotBlocked()) return UserStatus.BLOCKED;
        if(!user.isActive() && user.isNotBlocked() && tokenRepo.hasValidActivationToken(userId)) return UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION;
        if(!user.isActive() && user.isNotBlocked()  && tokenRepo.lastActivationTokenHasExpired(userId)) return UserStatus.STANDING_FOR_ACCOUNT_ACTIVATION_LINK;
        return  UserStatus.UN_KNOWN;
    }

    @Override
    public Page<ReadUserDTO> searchUsers(String key, Pageable pageable)
    {
        key = key == null ? "" : StringUtils.stripAccentsToUpperCase(key);
        return userRepo.searchUsers(key, pageable);
    }

    @Override @Transactional
    public ReadUserDTO createUserAndFunction(CreateUserAndFunctionsDTO dto, ActionIdentifier ai) throws UnknownHostException, IllegalAccessException {
        CreateUserDTO userDto = dto.getCreateUserDTO();
        ReadUserDTO  user = this.createUser(userDto, ai);

        List<CreateInitialFncDTO> createInitialFncDTOs = dto.getCreateInitialFncDTOS();
        if(createInitialFncDTOs == null || createInitialFncDTOs.isEmpty()) return user;

        ReadFncDTO readFncDTO = createInitialFncDTOs.stream().map(createInitialFncDTO ->
                this.createUserInitialFonction(user, createInitialFncDTO, ai)).filter(isCurrentFunction).collect(Collectors.toList()).stream().findFirst().get();
        user.setCurrentFnc(readFncDTO);
        return user;
    }

    @Override
    public ReadUserDTO updateUserAndFunction(UpdateUserAndFncDTO dto, ActionIdentifier ai) throws UnknownHostException {
        UpdateUserDTO userDto = dto.getUpdateUserDTO();
        ReadUserDTO  user = this.updateUser(userDto, ai);
        List<UpdateFncDTO> updateFncDTOS = dto.getUpdateFncDTOS();
        if(updateFncDTOS == null || updateFncDTOS.isEmpty()) return user;

        ReadFncDTO readFncDTO = updateFncDTOS.stream().map(updateFncDTO ->
        {
            try
            {
                return functionService.updateFunction(updateFncDTO, ai);
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
                return null;
            }
        }).filter(isCurrentFunction).collect(Collectors.toList()).stream().findFirst().get();

        user.setCurrentFnc(readFncDTO);
        return user;
    }

    private Predicate<ReadFncDTO> isCurrentFunction = (readFncDTO)->readFncDTO != null && readFncDTO.getFncStatus() == 1;

    private ReadFncDTO createUserInitialFonction(ReadUserDTO user, CreateInitialFncDTO dto, ActionIdentifier ai){
        CreateFncDTO createFncDTO = new CreateFncDTO();
        BeanUtils.copyProperties(dto, createFncDTO);
        createFncDTO.setUserId(user.getUserId());
        return functionService.createFnc(createFncDTO, ai);
    }

    @Override
    public ReadUserDTO getUserInfos(Long userId)
    {
        ReadUserDTO user = userRepo.findReadUserDto(userId);
        user.setCurrentFnc(functionService.getActiveCurrentFunction(userId));
        return user;
    }

    @Override
    public boolean existsByEmail(String email, Long userId) {
        return userRepo.alreadyExistsByEmail(email, userId);
    }

    @Override
    public boolean existsByTel(String tel, Long userId) {
        return userRepo.alreadyExistsByTel(tel, userId);
    }

    @Override
    public UpdateUserAndFncDTO getUpdateUserAndFunction(Long userId)
    {
        UpdateUserAndFncDTO dto = new UpdateUserAndFncDTO();
        UpdateUserDTO userDTO = userRepo.getUpdateUserDTO(userId);
        List<UpdateFncDTO> updateFncDTOS = fncRepo.getUpdateFncDTOSByUserId(userId);
        dto.setUpdateUserDTO(userDTO);
        if(updateFncDTOS == null || updateFncDTOS.isEmpty()) return dto;
        updateFncDTOS.stream().forEach(f->f.setRoleCodes(rtfRepo.getFncRoleCodes(f.getFncId())));
        dto.setUpdateFncDTOS(updateFncDTOS);
        return dto;
    }
}
