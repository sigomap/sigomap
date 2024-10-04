package ci.dgmp.sigomap.archivemodule.controller.service;

import org.springframework.stereotype.Component;
import ci.dgmp.sigomap.archivemodule.controller.repositories.DocumentRepository;
import ci.dgmp.sigomap.archivemodule.model.dtos.DocMapper;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UploadDocReq;
import ci.dgmp.sigomap.archivemodule.model.entities.Document;
import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;

@Component(value = "association")
public class AssociationDocUploader extends AbstractDocumentService
{
	public AssociationDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo, ILogService logService) {
		super(typeRepo, docMapper, docRepo, logService);
	}
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToMembreDoc(dto);
	}
}