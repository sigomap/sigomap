package ci.dgmp.sigomap.archivemodule.controller.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UpdateDocReq;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UploadDocReq;
import ci.dgmp.sigomap.archivemodule.model.dtos.response.ReadDocDTO;

import java.io.IOException;
import java.net.UnknownHostException;

public interface IServiceDocument
{
	void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException;
	byte[] downloadFile(String filePAth);

    @Transactional
	boolean uploadDocument(UploadDocReq dto) throws UnknownHostException;

	@Transactional
	boolean deleteDocument(Long docId) throws UnknownHostException;

	@Transactional
    boolean updateDocument(UpdateDocReq dto) throws IOException;

    void displayPdf(HttpServletResponse response, byte[] reportBytes, String displayName)  throws Exception;
	boolean deleteFile(String filePath);
	String generatePath(MultipartFile file, String objectFolder, String typeCode, String objectName);
	void renameFile(String oldPath, String newPath);

	Page<ReadDocDTO> getAllDocsForObject(Long userId, Long assoId, Long sectionId, String key, Pageable pageable);
}
