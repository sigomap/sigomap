package ci.dgmp.sigomap.authmodule.controller.services.impl;

import ci.dgmp.sigomap.authmodule.controller.repositories.PrvRepo;
import ci.dgmp.sigomap.authmodule.controller.repositories.PrvToRoleAssRepo;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IPrivilegeService;
import ci.dgmp.sigomap.authmodule.model.constants.AuthActions;
import ci.dgmp.sigomap.authmodule.model.constants.AuthTables;
import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.*;
import ci.dgmp.sigomap.authmodule.model.entities.AppPrivilege;
import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.ObjectCopier;
import ci.dgmp.sigomap.sharedmodule.utilities.StringUtils;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.dtos.ReadTypeDTO;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor @Slf4j
public class PrivilegeService implements IPrivilegeService
{
    private final PrvRepo prvRepo;
    private final PrvToRoleAssRepo prvToRoleAssRepo;
    private final PrivilegeMapper prvMapper;
    private final ILogService logger;
    private final TypeRepo typeRepo;
    private final ObjectCopier<AppPrivilege> prvCopier;

    @Override @Transactional
    public ReadPrvDTO createPrivilege(CreatePrivilegeDTO dto) throws UnknownHostException {
        AppPrivilege privilege = prvMapper.mapToPrivilege(dto);
        privilege = prvRepo.save(privilege);
        AppPrivilege oldPrv = new AppPrivilege();
        BeanUtils.copyProperties(privilege, oldPrv);
        logger.logg(AuthActions.CREATE_PRV, oldPrv, privilege, AuthTables.PRV_TABLE, null);
        return prvMapper.mapToReadPrivilegeDTO(privilege);
    }

    @Override
    public Page<ReadPrvDTO> searchPrivileges(String searchKey, List<String> typePrvUniqueCodes, Pageable pageable)
    {
        Page<AppPrivilege> privilegePage = typePrvUniqueCodes == null || typePrvUniqueCodes.isEmpty() ?
                prvRepo.searchPrivileges(StringUtils.stripAccentsToUpperCase(searchKey), pageable) :
                prvRepo.searchPrivileges(StringUtils.stripAccentsToUpperCase(searchKey), typePrvUniqueCodes, pageable) ;
        List<ReadPrvDTO> readPrvDTOS = privilegePage.stream().map(prvMapper::mapToReadPrivilegeDTO).collect(Collectors.toList());
        return new PageImpl<>(readPrvDTOS, pageable, privilegePage.getTotalElements());
    }

    @Override
    public List<SelectedPrvDTO> getSelectedPrvs(Set<String> roleCodes)
    {
        Set<String> selectedPrvCodes = prvToRoleAssRepo.findActivePrvCodesForRoles(roleCodes);
        return prvRepo.findAll().stream().map(prv->
                new SelectedPrvDTO(prv.getPrivilegeCode(), prv.getPrivilegeName(),
                        selectedPrvCodes.contains(prv.getPrivilegeCode()),
                        selectedPrvCodes.contains(prv.getPrivilegeCode()))).collect(Collectors.toList());
    }

    @Override
    public List<SelectedPrvDTO> getSelectedPrvs(Long fncId, Set<String> oldRoleCodes, Set<String> roleCodes, Set<String> prvCodes)
    {
        Set<String> ownedPrvCodes = prvRepo.getFunctionPrvCodes(fncId);
        //List<AppPrivilege> allPrvs = prvRepo.findAll();
        Set<String> selectedPrvCodes = prvToRoleAssRepo.findActivePrvCodesForRoles(roleCodes);

        //Set<Long> addedRoleCodes = roleCodes.stream().filter(rId-> !oldRoleCodes.contains(rId)).collect(Collectors.toSet());
        Set<String> retiredRoleCodes = oldRoleCodes.stream().filter(rId-> !roleCodes.contains(rId)).collect(Collectors.toSet());
        Set<String> prvCodesToBeRetired = retiredRoleCodes == null ? new HashSet<>() : retiredRoleCodes.isEmpty() ? new HashSet<>() : prvToRoleAssRepo.findActivePrvCodesForRoles(retiredRoleCodes).stream().filter(prvId->!selectedPrvCodes.contains(prvId)).collect(Collectors.toSet());
        prvCodes = prvCodes.stream().filter(prvId->!prvCodesToBeRetired.contains(prvId)).collect(Collectors.toSet());
        selectedPrvCodes.addAll(prvCodes);
        return prvRepo.findAll().stream().map(prv->new SelectedPrvDTO( prv.getPrivilegeCode(), prv.getPrivilegeName(), selectedPrvCodes.contains(prv.getPrivilegeCode()), ownedPrvCodes.contains(prv.getPrivilegeCode()))).collect(Collectors.toList());
    }

    @Override //TODO : Si la méthode de repository arrive à charger la liste des privilèges du type automatiquement, alors nous allons supprimer le chargement dans cette méthode ci
    public PrvByTypeDTO getPrivlegesByTypeCode(String typeCode)
    {
        Type type = typeRepo.findById(typeCode).orElseThrow(()->new AppException("Type in trouvable"));
        PrvByTypeDTO prvByTypeDTO = new PrvByTypeDTO(type.getName(), type.getUniqueCode());
        prvByTypeDTO.setPrivileges(prvRepo.getTypePriveleges(typeCode));
        return prvByTypeDTO;
    }

    @Override
    public List<PrvByTypeDTO> getAllPrivlegesGroupesByType()
    {
        List<PrvByTypeDTO> PrvByTypeDTOs = typeRepo.findTypeCodesByTypeGroup(TypeGroup.TYPE_PRV).stream().map(id->this.getPrivlegesByTypeCode(id)).filter(Objects::nonNull)
                .sorted(Comparator.comparing(PrvByTypeDTO::getTypeName)).collect(Collectors.toList());
        return PrvByTypeDTOs;
    }

    @Override
    public List<SelectOption> getPrivilegeTypes()
    {
        List<ReadTypeDTO> types = typeRepo.findByTypeGroup(TypeGroup.TYPE_PRV);
        return types.stream().map(t->new SelectOption( t.getUniqueCode(), t.getName())).collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name, String privilegeCode)
    {
        if(name == null || name.trim().equals("")) return false;
        if(privilegeCode == null || privilegeCode.trim().equals("")) return prvRepo.existsByName(name);
        return prvRepo.existsByName(name, privilegeCode);
    }

    @Override
    public boolean privilegeTypeIsValid(String typeCode) {
        return typeRepo.existsByGroupAndUniqueCode(TypeGroup.TYPE_PRV, typeCode);
    }

    @Override @Transactional
    public ReadPrvDTO updatePrivilege(UpdatePrivilegeDTO dto) throws UnknownHostException {
        AppPrivilege privilege = prvRepo.findById(dto.getPrivilegeCode()).orElseThrow(()->new AppException("Privilège introuvable"));
        AppPrivilege oldPrivilege = prvCopier.copy(privilege);
        privilege.setPrivilegeName(dto.getPrivilegeName());
        privilege.setPrvType(new Type(dto.getTypeCode()));
        logger.logg(AuthActions.UPDATE_PRV, oldPrivilege, privilege, AuthTables.PRV_TABLE, null);
        return prvMapper.mapToReadPrivilegeDTO(privilege);
    }

    @Override
    public List<SelectOption> getPrivilegesByTypes(List<String> typeCodes)
    {
        if(typeCodes == null || typeCodes.isEmpty()) return prvRepo.getAllOptions();
        return prvRepo.getPrivilegesByTypes(typeCodes);
    }

    @Override
    public List<SelectOption> getAllPrivilgesOptions()
    {
        return prvRepo.getAllOptions();
    }

    @Override
    public List<SelectOption> getPrivilegesOptionsByRoleCodes(Set<String> roleCodes)
    {
        List<SelectOption> prvs = prvToRoleAssRepo.findActivePrivilegesForRoles(roleCodes).stream()
                .map(p->new SelectOption(p.getPrivilegeCode(), p.getPrivilegeName()))
                .distinct()
                .collect(Collectors.toList());
        return prvs;
    }
}
