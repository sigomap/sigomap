package ci.dgmp.sigomap.archivemodule.controller.web;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ci.dgmp.sigomap.archivemodule.controller.repositories.DocumentRepository;
import ci.dgmp.sigomap.archivemodule.controller.service.AbstractDocumentService;
import ci.dgmp.sigomap.archivemodule.model.entities.Document;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;

@Controller @RequiredArgsConstructor @RequestMapping(path = "/documents") @ResponseStatus(HttpStatus.OK)
public class DocumentController
{
    private final AbstractDocumentService docService;
    private final DocumentRepository docRepo;
    private final TypeRepo typeRepo;

    @GetMapping(path = "/download")
    public byte[] downloadDocument(@PathVariable Long docId) throws Exception {
        Document doc = docRepo.findById(docId).orElse(null);
        if(doc == null) return null;
        String docPath = doc.getDocPath();
        return docService.downloadFile(docPath);
    }

    @GetMapping(path = "/display/{docId}")
    void displayDocument2(HttpServletResponse response, @PathVariable Long docId) throws Exception {
        Document doc = docRepo.findById(docId).orElse(null);
        if(doc == null) return;
        String docPath = doc.getDocPath();
        String name = FilenameUtils.getName(doc.getDocPath());
        byte[] docBytes = docService.downloadFile(docPath);

        docService.displayPdf(response, docBytes, name);
    }


}
