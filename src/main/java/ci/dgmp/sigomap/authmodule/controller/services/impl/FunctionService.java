package ci.dgmp.sigomap.authmodule.controller.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ci.dgmp.sigomap.authmodule.controller.repositories.FunctionRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.RoleToFunctionAssRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.UserRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IFunctionService;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IJwtService;
import ci.dgmp.sigomap.authmodule.model.constants.AuthActions;
import ci.dgmp.sigomap.authmodule.model.constants.AuthTables;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.CreateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.FncMapper;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.ReadFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appuser.AuthResponseDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.AssMapper;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.RoleAssSpliterDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.asignation.SetAuthoritiesToFunctionDTO;
import ci.dgmp.sigomap.authmodule.model.entities.*;
import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.ObjectCopier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FunctionService implements IFunctionService {

    @Lazy @Autowired private UserDetailsService uds;

    private final FunctionRepo functionRepo;
    private final RoleToFunctionAssRepo rtfRepo;
    private final UserRepo userRepo;
    private final FncMapper fncMapper;
    private final AssMapper assMapper;
    private final ILogService logger;
    private final ObjectCopier<RoleToFncAss> rtfCopier;
    private final IJwtService jwtService;

    @Override
    public Long getActiveCurrentFunctionId(Long userId)
    {
        List<Long> ids = functionRepo.getCurrentFncIds(userId);
        return ids == null ? null : ids.size() != 1 ? null : new ArrayList<>(ids).get(0);
    }

    @Override
    public String getActiveCurrentFunctionName(Long userId) {
        Set<String> fncNames = functionRepo.getCurrentFncNames(userId);
        return fncNames == null ? null : fncNames.size() != 1 ? null : new ArrayList<>(fncNames).get(0);
    }

    @Override @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public ReadFncDTO createFnc(CreateFncDTO dto, ActionIdentifier ai)
    {
        AppFunction function = fncMapper.mapToFunction(dto);
        boolean userHasFunction = functionRepo.userHasAnyAppFunction(dto.getUserId());
        function = functionRepo.save(function);
        BeanUtils.copyProperties(ai, function);
        Long fncId = function.getId();
        if(!userHasFunction)
        {
            function.setFncStatus(1);
            AppUser user = userRepo.findById(dto.getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable"));
            user.setCurrentFunctionId(fncId);
            user = userRepo.save(user);
            BeanUtils.copyProperties(ai, user);
        }
        Set<String> roleCodes = dto.getRoleCodes() == null ? new HashSet<>() : dto.getRoleCodes();

        roleCodes.forEach(code->
        {
            RoleToFncAss roleToFunctionAss = new RoleToFncAss();
            roleToFunctionAss.setAssStatus(1);
            roleToFunctionAss.setStartsAt(dto.getStartsAt());
            roleToFunctionAss.setEndsAt(dto.getEndsAt());
            roleToFunctionAss.setRole(new AppRole(code));
            roleToFunctionAss.setFunction(new AppFunction(fncId));
            roleToFunctionAss = rtfRepo.save(roleToFunctionAss);
            BeanUtils.copyProperties(ai, roleToFunctionAss);
        });

        return fncMapper.mapToReadFncDto(function);
    }

    @Override @Transactional
    public AuthResponseDTO setFunctionAsDefault(Long fncId, ActionIdentifier ai)
    {
        AppFunction function  = functionRepo.findById(fncId).orElseThrow(()->new AppException("Fonction inconnue"));
        if(function.getUser() == null ||function.getUser().getUserId() == null) throw new AppException("Utilisateur introuvable pour cette fonction");
        AppUser user = userRepo.findById(function.getUser().getUserId()).orElseThrow(()->new AppException("Utilisateur introuvable pour cette fonction"));
        UserDetails userDetails = uds.loadUserByUsername(user.getEmail());
        BeanUtils.copyProperties(ai, function);

        if(function.getFncStatus() == 1)
        {
            return jwtService.generateJwt(userDetails, jwtService.extractConnectionId());
        }

        function.setFncStatus(1);

        functionRepo.findActiveByUser(function.getUser().getUserId()).forEach(fnc->
        {
            if(!fnc.getId().equals(fncId) && fnc.getFncStatus() == 1)
            {
                fnc.setFncStatus(2);
                fnc = functionRepo.save(fnc);
                BeanUtils.copyProperties(ai, fnc);
            }
        });

        user.setCurrentFunctionId(fncId);
        user = userRepo.save(user);
        BeanUtils.copyProperties(ai, user);
        return jwtService.generateJwt(userDetails, UUID.randomUUID().toString());
    }

    @Override @Transactional
    public void revokeFunction(Long fncId, ActionIdentifier ai)
    {
        AppFunction function  = functionRepo.findById(fncId).orElse(null);
        if(function == null) return;
        if(function.getFncStatus() == 3) return;
        function.setFncStatus(3); functionRepo.save(function);
        BeanUtils.copyProperties(ai, function);
    }

    @Override @Transactional
    public void restoreFunction(Long fncId, ActionIdentifier ai)
    {
        AppFunction function  = functionRepo.findById(fncId).orElse(null);
        if(function == null) return;
        if(function.getFncStatus() == 1 || function.getFncStatus() == 2) return;
        function.setFncStatus(2); functionRepo.save(function);
        BeanUtils.copyProperties(ai, function);
    }

    //@Transactional
    private ReadFncDTO setFunctionAuthorities(SetAuthoritiesToFunctionDTO dto, ActionIdentifier ai)
    {
        AppFunction function  = functionRepo.findById(dto.getFncId()).orElse(null);
        if(function == null) return null;
        Long fncId = function.getId();
        Set<String> roleCodes = dto.getRoleCodes();
        LocalDate startsAt = dto.getStartsAt(); LocalDate endsAt = dto.getEndsAt();
        RoleAssSpliterDTO roleAssSpliterDTO = assMapper.mapToRoleAssSpliterDTO(roleCodes, fncId, startsAt, endsAt, true);
        treatRolesAssignation(roleAssSpliterDTO, fncId, startsAt, endsAt, ai);

        return fncMapper.mapToReadFncDto(function);
    }

    @Override @Transactional
    public ReadFncDTO updateFunction(UpdateFncDTO dto, ActionIdentifier ai)
    {
        AppFunction function  = functionRepo.findById(dto.getFncId()).orElse(null);
        if(function == null) return null;
        function.setName(dto.getName());
        function.setStartsAt(dto.getStartsAt());
        function.setEndsAt(dto.getEndsAt());
        BeanUtils.copyProperties(ai, function);
        ReadFncDTO readFncDTO = this.setFunctionAuthorities(new SetAuthoritiesToFunctionDTO(dto.getFncId(),dto.getStartsAt(), dto.getEndsAt(), dto.getRoleCodes()), ai);
        return readFncDTO;
    }

    @Override
    public ReadFncDTO getActiveCurrentFunction(Long userId)
    {
        Long currentFncId = this.getActiveCurrentFunctionId(userId);
        if(currentFncId == null) return null;
        AppFunction function = functionRepo.findById(currentFncId).orElseThrow(()->new AppException("Fonction introuvable"));
        ReadFncDTO readFncDTO = fncMapper.mapToReadFncDto(function);
        return readFncDTO;
    }

    @Override
    public ReadFncDTO getFunctioninfos(Long foncId)
    {
        AppFunction function = functionRepo.findById(foncId).orElseThrow(()-> new AppException("Fonction introuvable"));
        ReadFncDTO readFncDTO = fncMapper.mapToReadFncDto(function);
        return readFncDTO;
    }

    @Override
    public Page<ReadFncDTO> search(Long userId, String key, int page, int size, boolean withRevoked)
    {
        int[] allStatus = {1, 2, 3};
        int[] activeStatus = {1, 2};
        if(userId == null) return Page.empty();
        Page<AppFunction> functionsPage = functionRepo.findAllByUser(userId, key, withRevoked ? allStatus : activeStatus, PageRequest.of(page, size));
        if(functionsPage == null) return Page.empty();
        List<ReadFncDTO> functionsList = functionsPage.stream().map(fncMapper::mapToReadFncDto).toList();

        return new PageImpl<>(functionsList, functionsPage.getPageable(), functionsPage.getTotalElements());
    }

    private void treatRolesAssignation(RoleAssSpliterDTO roleAssSpliterDTO, Long fncId, LocalDate startsAt, LocalDate endsAt, ActionIdentifier ai)
    {
        roleAssSpliterDTO.getRoleCodesToBeRemoved().forEach(code->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, code);
            rtfAss.setAssStatus(2);
            rtfAss = rtfRepo.save(rtfAss);
            BeanUtils.copyProperties(ai, rtfAss);
        });

        roleAssSpliterDTO.getRoleCodesToBeAddedAsNew().forEach(code->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, code);
            if(rtfAss == null)
            {
                rtfAss = new RoleToFncAss();
                rtfAss.setAssStatus(1); rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
                rtfAss.setRole(new AppRole(code));
                rtfAss.setFunction(new AppFunction(fncId));
                rtfAss = rtfRepo.save(rtfAss);
                BeanUtils.copyProperties(ai, rtfAss);
            }
            else
            {
                RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
                rtfAss.setAssStatus(1);
                rtfRepo.save(rtfAss);
                BeanUtils.copyProperties(ai, rtfAss);
            }
        });

        roleAssSpliterDTO.getRoleCodesToChangeTheDates().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.CHANGE_ROLE_TO_FNC_VALIDITY_PERIOD, oldRtfAss, rtfAss, AuthTables.ASS, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleCodesToActivate().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(1);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.RESTORE_ROLE_TO_FNC, oldRtfAss, rtfAss, AuthTables.ASS, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        roleAssSpliterDTO.getRoleCodesToActivateAndChangeTheDates().forEach(id->
        {
            RoleToFncAss rtfAss = rtfRepo.findByFncAndRole(fncId, id);
            RoleToFncAss oldRtfAss = rtfCopier.copy(rtfAss);
            rtfAss.setAssStatus(1);rtfAss.setStartsAt(startsAt); rtfAss.setEndsAt(endsAt);
            rtfAss = rtfRepo.save(rtfAss);
            try {
                logger.logg(AuthActions.ASSIGNATION_ACTIVATED_AND_VALIDITY_PERIOD_CHANGED, oldRtfAss, rtfAss, AuthTables.ASS, null);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }
}
