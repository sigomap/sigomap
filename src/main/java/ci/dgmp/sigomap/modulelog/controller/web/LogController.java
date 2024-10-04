package ci.dgmp.sigomap.modulelog.controller.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import ci.dgmp.sigomap.modulelog.controller.service.ILogService;
import ci.dgmp.sigomap.modulelog.model.dtos.mapper.LogMapper;
import ci.dgmp.sigomap.modulelog.model.dtos.response.ConnexionList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor @ResponseStatus(HttpStatus.OK)
public class LogController {
    private final ILogService logService;
    private final LogMapper logMapper;

    @GetMapping("/connexion-list")
    public Page<ConnexionList> getConnexionList(@RequestParam(defaultValue = "") String key,
                                                @RequestParam(required = false) Long userId,
                                                @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy") LocalDate debut,
                                                @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy")LocalDate fin,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size)
    {
        return this.logService.getConnextionLogs(key,userId, debut, fin, PageRequest.of(page, size));
    }

    @GetMapping("/connexion-actions")
    public Page<ConnexionList> getConnexionActions(@RequestParam(defaultValue = "") String key,
                                                @RequestParam(required = false) Long userId,
                                                   @RequestParam String connId,
                                                @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy") LocalDate debut,
                                                @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy")LocalDate fin,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size)
    {
        return this.logService.getConnexionActionLogs(connId, key,userId, debut, fin, PageRequest.of(page, size));
    }


    @GetMapping("/system-errors")
    public Page<ConnexionList> getSystemErrors(@RequestParam(defaultValue = "") String key,
                                                   @RequestParam(required = false) Long userId,
                                                   @RequestParam(required = false) String connId,
                                                   @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy") LocalDate debut,
                                                   @RequestParam(required = false) @JsonFormat(pattern = "dd/MM/yyyy")LocalDate fin,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size)
    {
        return this.logService.getSystemErrors(connId, key,userId, debut, fin, PageRequest.of(page, size));
    }

    @DeleteMapping("/system-errors/delete")
    public void deleteSystemErrors(@RequestParam List<Long> errorIds)
    {
        logService.deleteSystemErrors(errorIds);
    }
}
