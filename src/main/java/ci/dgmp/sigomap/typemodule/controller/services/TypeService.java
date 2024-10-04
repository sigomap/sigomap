package ci.dgmp.sigomap.typemodule.controller.services;

import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import ci.dgmp.sigomap.sharedmodule.enums.PersStatus;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.ObjectCopier;
import ci.dgmp.sigomap.sharedmodule.utilities.StringUtils;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeParamRepo;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.constants.TypeActions;
import ci.dgmp.sigomap.typemodule.model.constants.TypeTables;
import ci.dgmp.sigomap.typemodule.model.dtos.*;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.entities.TypeParam;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service @Transactional
@RequiredArgsConstructor
public class TypeService implements ITypeService
{
    private final TypeRepo typeRepo;
    private final TypeMapper typeMapper;
    private final TypeParamRepo typeParamRepo;
    private final ILogService logger;
    private final ObjectCopier<Type> typeCopier;
    private final ObjectCopier<TypeParam> typeParamCopier;
    @Override
    public Type createType(CreateTypeDTO dto) throws UnknownHostException {
        Type type = typeMapper.mapToType(dto);
        type = typeRepo.save(type);
        logger.logg(TypeActions.CREATE_TYPE, null, type, TypeTables.TYPE, null);
        return type;
    }

    @Override @Transactional
    public Type updateType(UpdateTypeDTO dto) throws UnknownHostException {
        Type loadedType = typeRepo.findById(dto.getOldUniqueCode()).orElseThrow(()->new AppException("Type introuvable : " + dto.getOldUniqueCode()));
        Type oldType = typeCopier.copy(loadedType);

        loadedType.setTypeGroup(TypeGroup.valueOf(dto.getTypeGroup()));
        loadedType.setName(dto.getName().toUpperCase(Locale.ROOT));
        loadedType.setUniqueCode(dto.getUniqueCode().toUpperCase(Locale.ROOT));
        logger.logg(TypeActions.UPDATE_TYPE, oldType, loadedType, TypeTables.TYPE, null);

        return loadedType;
    }

    @Override
    public void deleteType(String uniqueCode) throws UnknownHostException {
        Type loadedType = uniqueCode == null ? null : typeRepo.findById(uniqueCode).orElse(null);
        if(loadedType == null || loadedType.getStatus() == PersStatus.DELETED) return;
        Type oldType = typeCopier.copy(loadedType);
        loadedType.setStatus(PersStatus.DELETED);
        logger.logg(TypeActions.DELETE_TYPE, oldType, loadedType, TypeTables.TYPE, null);
    }

    @Override
    public void restoreType(String uniqueCode) throws UnknownHostException {
        Type loadedType = uniqueCode == null ? null : typeRepo.findById(uniqueCode).orElse(null);
        if(loadedType == null || loadedType.getStatus() == PersStatus.ACTIVE) return;
        Type oldType = typeCopier.copy(loadedType);
        loadedType.setStatus(PersStatus.ACTIVE);
        logger.logg(TypeActions.RESTORE_TYPE, oldType, loadedType, TypeTables.TYPE, null);
    }

    @Override
    public List<SelectOption> getTypeGroupOptions() {
        List<SelectOption> options = Arrays.stream(TypeGroup.values()).map(tg->new SelectOption(tg.name(), tg.getGroupName())).collect(Collectors.toList());
        //options.add(0, new SelectOption("", ""));
        return options;
    }

    @Override
    public boolean existsByName(String name, String uniqueCode)
    {
        if(name == null || name.equals("")) return false;
        if(uniqueCode == null || uniqueCode.equals("")) return typeRepo.existsByName(name);
        return typeRepo.existsByName(name, uniqueCode);
    }

    @Override
    public boolean typeGroupIsValid(String typeGroup)
    {
        if(typeGroup == null || typeGroup.trim().equals("")) return false;
        return TypeGroup.hasValue(typeGroup);
    }

    @Override
    public boolean existsByUniqueCode(String uniqueCode, String oldUniqueCode)
    {
        if(uniqueCode == null || uniqueCode.trim().equals("")) return false;
        if(oldUniqueCode==null || oldUniqueCode.trim().equals("")) return typeRepo.existsByUniqueCode(uniqueCode);
        return typeRepo.existsByUniqueCode(uniqueCode, oldUniqueCode);
    }

    @Override
    public List<SelectOption> getOptions(TypeGroup typeGroup)
    {
        return typeRepo.findOptionsByTypeGroup(typeGroup);
    }

    @Override @Transactional
    public void addSousType(TypeParamDTO dto) throws UnknownHostException {
        if(this.parentHasDistantSousType(dto.getChildCode(), dto.getParentCode())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentCode(), dto.getChildCode())) return;
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentCode(), dto.getChildCode()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentCode(), dto.getChildCode());
            TypeParam oldTypeParam = typeParamCopier.copy(typeParam);
            typeParam.setStatus(PersStatus.ACTIVE);
            logger.logg(TypeActions.RESTORE_SUB_TYPE, oldTypeParam, typeParam, TypeTables.TYPE_PARAM, null);
            return;
        }
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setStatus(PersStatus.ACTIVE);
        typeParam = typeParamRepo.save(typeParam);
        logger.logg(TypeActions.ADD_SUB_TYPE, null, typeParam, TypeTables.TYPE_PARAM, null);
    }


    @Override @Transactional
    public void setSousTypes(TypeParamsDTO dto)
    {
        List<String> alreadyExistingSousTypeCodes = typeRepo.findChildrenCodes(dto.getParentCode());
        Set<String> newSousTypesToSetCodes = Arrays.stream(dto.getChildCodes()).filter(id0-> alreadyExistingSousTypeCodes.stream().noneMatch(id0::equals)).collect(Collectors.toSet());
        Set<String> sousTypesToRemoveCodes = alreadyExistingSousTypeCodes.stream().filter(id0-> Arrays.stream(dto.getChildCodes()).noneMatch(id0::equals)).collect(Collectors.toSet());

        newSousTypesToSetCodes.stream().map(code->new TypeParamDTO(code, dto.getParentCode())).forEach(o-> {
            try {
                this.addSousType(o);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        sousTypesToRemoveCodes.stream().map(id->new TypeParamDTO(id, dto.getParentCode())).forEach(o-> {
            try {
                this.removeSousType(o);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });
    }

    @Override @Transactional
    public void removeSousType(TypeParamDTO dto) throws UnknownHostException {
        if(typeParamRepo.alreadyExistsAndNotActive(dto.getParentCode(), dto.getChildCode())) return;
        if(typeParamRepo.alreadyExistsAndActive(dto.getParentCode(), dto.getChildCode()))
        {
            TypeParam typeParam = typeParamRepo.findByParentAndChild(dto.getParentCode(), dto.getChildCode());
            TypeParam oldTypeParam = typeParamCopier.copy(typeParam);
            typeParam.setStatus(PersStatus.DELETED);
            logger.logg(TypeActions.REMOVE_SUB_TYPE, oldTypeParam, typeParam, TypeTables.TYPE_PARAM, null);
            return;
        }
        TypeParam typeParam = typeMapper.mapToTypeParam(dto);
        typeParam.setStatus(PersStatus.DELETED);
        typeParam = typeParamRepo.save(typeParam);
        logger.logg(TypeActions.REMOVE_SUB_TYPE, null, typeParam, TypeTables.TYPE_PARAM, null);

    }

    @Override
    public boolean parentHasDirectSousType(String parentCode, String childCode)
    {
        return typeParamRepo.parentHasDirectSousType(parentCode, childCode);
    }

    @Override
    public boolean parentHasDistantSousType(String parentCode, String childCode)
    {
        if(parentHasDirectSousType(parentCode, childCode)) return true;
        if(!typeRepo.existsById(parentCode) || !typeRepo.existsById(childCode)) return false;
        return typeRepo.findActiveSousTypes(parentCode).stream().anyMatch(st->parentHasDistantSousType(st.getUniqueCode(), childCode));
    }

    @Override
    public List<Type> getPossibleSousTypes(String parentCode)
    {
        return typeRepo.findByTypeGroupAndStatus(typeRepo.findTypeGroupByTypeCode(parentCode), PersStatus.ACTIVE).stream().filter(t->!this.parentHasDistantSousType(t.getUniqueCode(), parentCode) && !t.getUniqueCode().equals(parentCode)).collect(Collectors.toList());
    }

    @Override
    public Type setSousTypesRecursively(String typeCode)
    {
        Type type = typeRepo.findById(typeCode).orElse(null);
        if(type == null) return null;
        List<Type> sousTypes = typeRepo.findActiveSousTypes(typeCode);
        type.setChildren(sousTypes);
        sousTypes.forEach(t->setSousTypesRecursively(t.getUniqueCode()));
        return type;
    }

    @Override
    public List<Type> getSousTypesRecursively(String uniqueCode)
    {
        Type type = typeRepo.findById(uniqueCode).orElse(null);
        if(type == null) return null;
        return typeRepo.findActiveSousTypes(uniqueCode).stream().flatMap(t-> Stream.concat(Stream.of(t), getSousTypesRecursively(t.getUniqueCode()).stream())).collect(Collectors.toList());
    }

    @Override
    public List<TypeGroup> getTypeGroups() {
        return Arrays.asList(TypeGroup.values());
    }

    @Override
    public Page<Type> searchPageOfTypes(String key, List<String> typeGroups, int pageNum, int pageSize)
    {
        //if("".equals(key)) return typeRepo.searchPageOfTypes(PersStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
        List<TypeGroup> baseTypeGroups = typeGroups == null || typeGroups.isEmpty() ?
                Arrays.stream(TypeGroup.values()).toList()
                : typeGroups.stream().filter(tg-> TypeGroup.hasValue(tg)).map(tg->TypeGroup.valueOf(tg)).collect(Collectors.toList());

        return typeRepo.searchPageOfTypes(key, baseTypeGroups, PersStatus.ACTIVE, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public Page<Type> searchPageOfDeletedTypes(String key, String typeGroup, int pageNum, int pageSize)
    {
        if("".equals(key)) return typeRepo.searchPageOfTypes(PersStatus.DELETED, PageRequest.of(pageNum, pageSize));
        Set<TypeGroup> typeGroups =  Arrays.asList(TypeGroup.values()).stream().filter(tg ->StringUtils.containsIgnoreCase(tg.getGroupCode(), key)
                || StringUtils.containsIgnoreCase(tg.getGroupName(), key) || StringUtils.containsIgnoreCase(tg.name(), key)).collect(Collectors.toSet());
        if(typeGroups.size() == 0) return typeRepo.searchPageOfTypes(key, PersStatus.DELETED, PageRequest.of(pageNum, pageSize));
        return typeRepo.searchPageOfTypes(key, typeGroups, PersStatus.DELETED, PageRequest.of(pageNum, pageSize));
    }
}