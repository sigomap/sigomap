package ci.dgmp.sigomap.archivemodule.model.dtos;

import ci.dgmp.sigomap.archivemodule.model.dtos.request.UploadDocReq;
import ci.dgmp.sigomap.archivemodule.model.entities.Document;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DocMapper
{
    @Autowired protected TypeRepo typeRepo;

    @Mapping(target = "docDescription", expression = "java(\"Photo de profil\")")
    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new ci.dgmp.sigomap.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "user", expression = "java(new ci.dgmp.sigomap.authmodule.model.entities.AppUser(dto.getObjectId()))")
    @Mapping(target = "file", expression = "java(ci.dgmp.sigomap.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToPhotoDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new ci.dgmp.sigomap.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "file", expression = "java(ci.dgmp.sigomap.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToAssociationDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new ci.dgmp.sigomap.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "file", expression = "java(ci.dgmp.sigomap.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToSectionDoc(UploadDocReq dto);

    @Mapping(target = "docType", expression = "java(typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new ci.dgmp.sigomap.sharedmodule.exceptions.AppException(\"Type de document inconnu\")))")
    @Mapping(target = "user", expression = "java(new ci.dgmp.sigomap.authmodule.model.entities.AppUser(dto.getObjectId()))")
    @Mapping(target = "file", expression = "java(ci.dgmp.sigomap.sharedmodule.utilities.Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()))")
    public abstract Document mapToMembreDoc(UploadDocReq dto);
}