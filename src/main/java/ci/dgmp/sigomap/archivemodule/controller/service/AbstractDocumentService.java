package ci.dgmp.sigomap.archivemodule.controller.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ci.dgmp.sigomap.archivemodule.controller.repositories.DocumentRepository;
import ci.dgmp.sigomap.archivemodule.model.constants.ArchiveActions;
import ci.dgmp.sigomap.archivemodule.model.constants.ArchiveTable;
import ci.dgmp.sigomap.archivemodule.model.constants.DocumentsConstants;
import ci.dgmp.sigomap.archivemodule.model.dtos.DocMapper;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UpdateDocReq;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UploadDocReq;
import ci.dgmp.sigomap.archivemodule.model.dtos.response.ReadDocDTO;
import ci.dgmp.sigomap.archivemodule.model.entities.Document;
import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.Base64ToFileConverter;
import ci.dgmp.sigomap.sharedmodule.utilities.ObjectCopier;
import ci.dgmp.sigomap.sharedmodule.utilities.StringUtils;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

@Component @RequiredArgsConstructor
public abstract class AbstractDocumentService implements IServiceDocument
{
	protected final TypeRepo typeRepo;
	protected final DocMapper docMapper;
	protected final DocumentRepository docRepo;
	protected final ILogService logService;
	@Autowired protected  ObjectCopier<Document> docCopier;

	@Override
	public byte[] downloadFile(String filePAth)
	{
		File file = new File(filePAth);
		Path path = Paths.get(file.toURI());
		try
		{
			return Files.readAllBytes(path);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new AppException("Erreur de téléchargement");
		}
	}

	@Override
	public boolean deleteFile(String filePath)
	{
		File file = new File(filePath);
		return file == null ? false : file.delete();
	}

	protected abstract Document mapToDocument(UploadDocReq dto);


	@Override
	public String generatePath(MultipartFile file, String objectFolder, String typeCode, String objectName)
	{
		if(!typeRepo.existsByUniqueCode(typeCode)) return "";
		String uuid = UUID.randomUUID().toString().substring(0, 9);

		return DocumentsConstants.UPLOADS_DIR + File.separator + objectFolder+ File.separator  +typeCode + File.separator +
				StringUtils.stripAccents(objectName).replace(" ", "_") + uuid + "." + FilenameUtils.getExtension(file.getOriginalFilename());
	}

	@Override
	public void renameFile(String oldPath, String newPath)
	{
		if(new File(oldPath).exists())
		{
			try {
				Files.move(Paths.get(oldPath), Paths.get(newPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException
	{
		try
		{
			Files.write(Paths.get(destinationPath), file.getBytes());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Transactional @Override
	public boolean uploadDocument(UploadDocReq dto) throws UnknownHostException {
		if(dto.getDocUniqueCode() == null ) throw new AppException("Le type de document ne peut être null");
		Type docType = typeRepo.findById(dto.getDocUniqueCode().toUpperCase(Locale.ROOT)).orElseThrow(()->new AppException("Type de document inconnu"));
		if(docType == null || docType.getTypeGroup() != TypeGroup.DOCUMENT)  throw new AppException("Ce type de document n'est pris en charge par le système");;
		Document doc = mapToDocument(dto);
		String path = generatePath(dto.getFile(), docType.getObjectFolder(), dto.getDocUniqueCode(), docType.getName());
		doc.setDocPath(path);

		uploadFile(dto.getFile(), doc.getDocPath());
		doc = docRepo.save(doc);
		//logService.logg(ArchiveActions.UPLOAD_DOCUMENT, null, doc, ArchiveTable.DOCUMENT, );
		return true;
	}

	@Transactional @Override
	public boolean deleteDocument(Long docId) throws UnknownHostException {
		Document doc = docRepo.findById(docId).orElseThrow(()->new AppException("Document inexistant"));
		docRepo.deleteById(docId);
		//logService.logg(ArchiveActions.DELETE_DOCUMENT, doc, new Document(), ArchiveTable.DOCUMENT);
		this.deleteFile(doc.getDocPath());
		return true;
	}

	@Transactional @Override
	public boolean updateDocument(UpdateDocReq dto) throws IOException {
		Document doc = docRepo.findById(dto.getDocId()).orElseThrow(()->new AppException("Document inexistant"));

		Document oldDoc = docCopier.copy(doc);
		doc.setDocNum(dto.getDocNum());
		doc.setDocDescription(dto.getDocDescription());
		doc.setDocName(dto.getDocName());
		String oldBase64UrlFile = Base64ToFileConverter.getBase64UrlFromPath(doc.getDocPath());
		Type newType = typeRepo.findById(dto.getDocUniqueCode()).orElseThrow(()->new AppException("Type de document inconnu"));
		if(!doc.getDocType().getUniqueCode().equals(newType.getUniqueCode()))
		{
			doc.setDocType(newType);
			MultipartFile file = Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), "." + dto.getExtension());
			String path = generatePath(file, newType.getObjectFolder(), dto.getDocUniqueCode(), doc.getDocDescription());
			doc.setDocPath(path);
			uploadFile(Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), dto.getExtension()), path);
		}
		else if(!oldBase64UrlFile.equals(dto.getBase64UrlFile().replace("+", "-").replace("/", "_")))
		{
			MultipartFile file = Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), "." + dto.getExtension());
			String path = generatePath(file, newType.getObjectFolder(), dto.getDocUniqueCode(), newType.getName());
			doc.setDocPath(path);
			uploadFile(file, path);
		}
		this.deleteFile(oldDoc.getDocPath());
		//logService.logg(ArchiveActions.UPLOAD_DOCUMENT, doc, new Document(), ArchiveTable.DOCUMENT);
		return true;
	}

	@Override
	public void displayPdf(HttpServletResponse response, byte[] fileBytes, String displayName)  throws Exception
	{
		// Configurez l'en-tête de la réponse HTTP
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "inline; filename=" + displayName +".pdf");
		response.setContentLength(fileBytes.length);

		// Écrivez le rapport Jasper dans le flux de sortie de la réponse HTTP
		OutputStream outStream = response.getOutputStream();
		outStream.write(fileBytes);
		outStream.flush();
		outStream.close();
	}

	@Override
	public Page<ReadDocDTO> getAllDocsForObject(Long userId, Long assoId, Long sectionId, String key, Pageable pageable)
	{
		key = key == null || key.trim().equals("") ? "" : StringUtils.stripAccentsToUpperCase(key);
		return docRepo.getAllDocsForObject(userId, key, pageable);
	}
}