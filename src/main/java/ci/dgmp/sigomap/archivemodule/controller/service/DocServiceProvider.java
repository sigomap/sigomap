package ci.dgmp.sigomap.archivemodule.controller.service;

import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class DocServiceProvider
{
    private final AssociationDocUploader assDocUploader;
    private final SectionDocUploader sectionDocUploader;
    private final MembreDocUploader membreDocUploader;
    private final TypeRepo typeRepo;

    public AbstractDocumentService getDocUploader(String typeDocUniqueCode)
    {
        if(typeDocUniqueCode == null) return null;
        Type typeDoc = typeRepo.findById(typeDocUniqueCode.toUpperCase()).orElseThrow(()->new AppException("Type de document inconnu"));
        if(typeDoc == null || typeDoc.getTypeGroup() != TypeGroup.DOCUMENT) return null;

        return switch (typeDoc.getUniqueCode())
                {
                    case "DOC_MBR"->membreDocUploader;
                    case "DOC_ASS"->assDocUploader;
                    case "DOC_SEC"->sectionDocUploader;
                    default -> null;
                };
    }
}