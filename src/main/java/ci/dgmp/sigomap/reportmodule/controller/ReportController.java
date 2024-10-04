package ci.dgmp.sigomap.reportmodule.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import ci.dgmp.sigomap.archivemodule.controller.service.AbstractDocumentService;
import ci.dgmp.sigomap.reportmodule.config.JasperReportConfig;
import ci.dgmp.sigomap.reportmodule.service.IServiceReport;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//@Controller @RequestMapping(path = "/reports")
@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class ReportController
{
    private final IServiceReport jrService;
    private final JasperReportConfig jrConfig;
    private final AbstractDocumentService docService;
}
