package ci.dgmp.sigomap.typemodule.model.dtos;

import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.entities.TypeParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TypeMapper
{
    @Mapping(target = "status", expression = "java(ci.dgmp.sigomap.sharedmodule.enums.PersStatus.ACTIVE)")
    @Mapping(target = "typeGroup", expression = "java(ci.dgmp.sigomap.typemodule.model.enums.TypeGroup.valueOf(dto.getTypeGroup()))")
    @Mapping(target = "uniqueCode", expression = "java(dto.getUniqueCode().toUpperCase())")
    @Mapping(target = "name", expression = "java(dto.getName().toUpperCase())")
    Type mapToType(CreateTypeDTO dto);

    Type mapToType(UpdateTypeDTO dto);

    @Mapping(target = "typeGroup", expression = "java(type.getTypeGroup().getGroupName())")
    ReadTypeDTO mapToReadTypeDTO(Type type);
    @Mapping(target = "parent.uniqueCode", source = "dto.parentCode")
    @Mapping(target = "child.uniqueCode", source = "dto.childCode")
    TypeParam mapToTypeParam(TypeParamDTO dto);
}