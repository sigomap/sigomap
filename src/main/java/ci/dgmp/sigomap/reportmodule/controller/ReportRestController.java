package ci.dgmp.sigomap.reportmodule.controller;

import ci.dgmp.sigomap.archivemodule.controller.service.AbstractDocumentService;
import ci.dgmp.sigomap.archivemodule.model.dtos.response.Base64FileDto;
import ci.dgmp.sigomap.reportmodule.config.JasperReportConfig;
import ci.dgmp.sigomap.reportmodule.service.IServiceReport;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.Base64ToFileConverter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController @RequestMapping(path = "/reports") @RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class ReportRestController
{
    private final IServiceReport jrService;
    private final JasperReportConfig jrConfig;
    private final AbstractDocumentService docService;

}
