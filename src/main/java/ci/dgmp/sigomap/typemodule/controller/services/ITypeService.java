package ci.dgmp.sigomap.typemodule.controller.services;


import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import ci.dgmp.sigomap.typemodule.model.dtos.*;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;
import ci.dgmp.sigomap.typemodule.model.dtos.CreateTypeDTO;
import org.springframework.data.domain.Page;

import java.net.UnknownHostException;
import java.util.List;

public interface ITypeService
{
    Type createType(CreateTypeDTO dto) throws UnknownHostException;
    Type updateType(UpdateTypeDTO dto) throws UnknownHostException;
    void deleteType(String typeCode) throws UnknownHostException;
    void addSousType(TypeParamDTO dto) throws UnknownHostException;
    void removeSousType(TypeParamDTO dto) throws UnknownHostException;
    void setSousTypes(TypeParamsDTO dto) throws UnknownHostException;
    boolean parentHasDirectSousType(String parentCode, String childCode);
    boolean parentHasDistantSousType(String parentCode, String childCode);

    List<Type> getPossibleSousTypes(String parentId);

    Type setSousTypesRecursively(String typeCode);
    List<Type> getSousTypesRecursively(String typeCode);
    List<TypeGroup> getTypeGroups();

    Page<Type> searchPageOfTypes(String key, List<String> typeGroups, int pageNum, int pageSize);
    Page<Type> searchPageOfDeletedTypes(String key, String typeGroup, int pageNum, int pageSize);

    void restoreType(String typeCode) throws UnknownHostException;

    List<SelectOption> getTypeGroupOptions();

    boolean existsByName(String name, String uniqueCode);

    boolean typeGroupIsValid(String typeGroup);

    boolean existsByUniqueCode(String uniqueCode, String oldUniqueCode);

    List<SelectOption> getOptions(TypeGroup typeFrequence);
}
