package ci.dgmp.sigomap.archivemodule.controller.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ci.dgmp.sigomap.archivemodule.controller.repositories.DocumentRepository;
import ci.dgmp.sigomap.archivemodule.controller.service.AbstractDocumentService;
import ci.dgmp.sigomap.archivemodule.controller.service.DocServiceProvider;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UpdateDocReq;
import ci.dgmp.sigomap.archivemodule.model.dtos.request.UploadDocReq;
import ci.dgmp.sigomap.archivemodule.model.dtos.response.Base64FileDto;
import ci.dgmp.sigomap.archivemodule.model.dtos.response.ReadDocDTO;
import ci.dgmp.sigomap.archivemodule.model.entities.Document;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.Base64ToFileConverter;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.dtos.ReadTypeDTO;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController @RequiredArgsConstructor @RequestMapping(path = "/documents") @ResponseStatus(HttpStatus.OK)
public class DocumentRestController
{
    private final DocumentRepository docRepo;
    private final TypeRepo typeRepo;
    private final DocServiceProvider docServiceProvider;
    private final AbstractDocumentService docService;

    @GetMapping(path = "/{typeDocUniqueCode}/types")
    public List<ReadTypeDTO> getTypeDocumentReglement(@PathVariable String typeDocUniqueCode) throws UnknownHostException {
        Type typeDoc = typeDocUniqueCode == null ? null : typeRepo.findById(typeDocUniqueCode.toUpperCase()).orElseThrow(()->new AppException("Type de document inconnu"));
        if(typeDoc == null) return new ArrayList<>();
        if(typeDoc.getTypeGroup() != TypeGroup.DOCUMENT) return new ArrayList<>();

        return typeRepo.findSousTypeOf(typeDoc.getUniqueCode());
    }

    @PostMapping(path = "/{groupDocUniqueCode}/upload2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean uploadDocument(@RequestParam MultipartFile file, @RequestParam Long objectId, @RequestParam String docNum, @RequestParam String docDescription, @RequestParam String typeDocUniqueCode, @PathVariable String groupDocUniqueCode, @PathVariable String docName) throws IOException {
        AbstractDocumentService docUploader = docServiceProvider.getDocUploader(groupDocUniqueCode);
        String base64FileString = Base64ToFileConverter.convertToBase64UrlString(file);
        if(docUploader == null)  throw new AppException("Ce type de document n'est pas pris en charge par le système");
        UploadDocReq dto = new UploadDocReq(objectId, typeDocUniqueCode.toUpperCase(Locale.ROOT), docNum, docName,docDescription, file);
        docUploader.uploadDocument(dto);
        return true;
    }

    @PostMapping(path = "/{groupDocUniqueCode}/upload")
    public boolean uploadDocument(@RequestBody UploadDocReq dto, @PathVariable String groupDocUniqueCode) throws UnknownHostException
    {
        groupDocUniqueCode = groupDocUniqueCode == null || groupDocUniqueCode.trim().equals("") ? groupDocUniqueCode : groupDocUniqueCode.replace("-", "_");
        AbstractDocumentService docUploader = docServiceProvider.getDocUploader(groupDocUniqueCode);
        if(docUploader == null)  throw new AppException("Ce type de document n'est pas pris en charge par le système");
        dto.setFile(Base64ToFileConverter.convertToFile(dto.getBase64UrlFile(), "." + dto.getExtension()));
        docUploader.uploadDocument(dto);
        return true;
    }

    @PutMapping(path = "/update")
    public boolean updateDocument(@Valid @RequestBody UpdateDocReq dto) throws IOException {
        return docService.updateDocument(dto);
    }

    @DeleteMapping(path = "/delete/{docId}")
    public boolean deleteDocument(@PathVariable Long docId) throws IOException {
        return docService.deleteDocument(docId);
    }

    @GetMapping(path = "/users/{userId}")
    public Page<ReadDocDTO> getAffDocs(@PathVariable Long userId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "") String key)
    {
        return docService.getAllDocsForObject(userId, null, null, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/associations/{assoId}")
    public Page<ReadDocDTO> getPlaDocs(@PathVariable Long assoId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "") String key)
    {
        return docService.getAllDocsForObject(null, assoId, null, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/sections/{sectionId}")
    public Page<ReadDocDTO> getRegDocs(@PathVariable Long sectionId,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "") String key)
    {
        return docService.getAllDocsForObject(null, null, sectionId, key, PageRequest.of(page, size));
    }

    @GetMapping(path = "/get-base64-url/{docId}")
    public Base64FileDto displayDocument(@PathVariable Long docId) throws Exception
    {
        Document doc = docRepo.findById(docId).orElse(null);
        if(doc == null) return null;
        String docPath = doc.getDocPath();
        byte[] docBytes = docService.downloadFile(docPath);
        String base64UrlString = Base64ToFileConverter.convertBytesToBase64UrlString(docBytes).replace("_", "/").replace("-", "+");
        return new Base64FileDto(base64UrlString, docBytes);
    }
}