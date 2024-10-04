package ci.dgmp.sigomap.archivemodule.controller.service;

import ci.dgmp.sigomap.archivemodule.controller.repositories.DocumentRepository;
import ci.dgmp.sigomap.archivemodule.model.dtos.DocMapper;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UploadDocReq;
import ci.dgmp.sigomap.archivemodule.model.entities.Document;
import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import org.springframework.stereotype.Component;

@Component("section")
public class SectionDocUploader extends AbstractDocumentService
{
	public SectionDocUploader(TypeRepo typeRepo, DocMapper docMapper, DocumentRepository docRepo, ILogService logService) {
		super(typeRepo, docMapper, docRepo, logService);
	}
	@Override
	protected Document mapToDocument(UploadDocReq dto) {
		return docMapper.mapToAssociationDoc(dto);
	}
}