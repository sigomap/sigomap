package ci.dgmp.sigomap.structuremodule.model.dtos;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ci.dgmp.sigomap.structuremodule.controller.repositories.StrRepo;
import ci.dgmp.sigomap.structuremodule.model.entities.Structure;
import ci.dgmp.sigomap.typemodule.model.entities.Type;

@Mapper(componentModel = "spring")
public abstract class StrMapper
{
    @Autowired
    protected StrRepo strRepo;
    @PersistenceContext
    protected EntityManager entityManager;

    @Mapping(target = "strType", source = "str.strType.name")
    @Mapping(target = "parentId", source ="strParent.strId")
    @Mapping(target = "parentName", source ="strParent.strName")
    @Mapping(target = "parentSigle", source ="strParent.strSigle")
    public abstract ReadStrDTO mapToReadStrDTO(Structure str);

    public ReadStrDTO mapToReadSimpleReadStrDto(Structure str)
    {
        ReadStrDTO str0 = new ReadStrDTO();
        str0.setStrId(str.getStrId());
        str0.setStrSigle(str.getStrSigle());
        str0.setHierarchySigles(strRepo.getHierarchySigle(str.getStrId()));
        str0.setStrName(str.getStrName());
        return str0;
    }


    @Mapping(target = "strParent.strId", expression = "java(createStrDTO.getParentId()==null ? strRepo.getStrIdByStrCode(\"SYS\") : createStrDTO.getParentId())")
    @Mapping(target = "strType.uniqueCode", source = "typeCode")
    @Mapping(target = "status", expression = "java(ci.dgmp.sigomap.sharedmodule.enums.PersStatus.ACTIVE)")
    public abstract Structure mapToStructure(CreateStrDTO createStrDTO);

    public abstract UpdateStrDTO mapToUpdateStrDTO(Structure str);

    public Structure mapToStructure(UpdateStrDTO dto)
    {
        if(dto==null) return null;
        Structure loadedStructure = strRepo.findById(dto.getStrId()).orElse(null);
        if(loadedStructure == null ) return null;
        entityManager.detach(loadedStructure);
        loadedStructure.setStrName(dto.getStrName());
        loadedStructure.setStrSigle(dto.getStrSigle());
        loadedStructure.setStrTel(dto.getStrTel());
        loadedStructure.setStrAddress(dto.getStrAddress());
        loadedStructure.setSituationGeo(dto.getSituationGeo());
        loadedStructure.setCreationActFile(dto.getCreationActFile());
        return loadedStructure;
    }

    public Structure mapToStructure(ChangeAnchorDTO dto)
    {
        if(dto==null) return null;
        Structure loadedStructure = strRepo.findById(dto.getStrId()).orElse(null);
        if(loadedStructure == null ) return null;
        entityManager.detach(loadedStructure);
        loadedStructure.setStrType(new Type(dto.getNewTypeCode()));
        loadedStructure.setStrParent(dto.getNewParentId() == null ? new Structure(strRepo.getStrIdByStrCode("SYS")) : new Structure(dto.getNewParentId()));
        loadedStructure.setStrLevel(dto.getNewParentId() == null ? 1 : strRepo.getStrLevel(dto.getNewParentId())+1);
        loadedStructure.setStrName(dto.getNewStrName());
        loadedStructure.setStrSigle(dto.getNewStrSigle());
        return loadedStructure;
    }
}