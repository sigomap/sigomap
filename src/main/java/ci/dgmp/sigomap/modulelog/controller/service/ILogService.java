package ci.dgmp.sigomap.modulelog.controller.service;

import ci.dgmp.sigomap.modulelog.model.dtos.response.ConnexionList;
import ci.dgmp.sigomap.modulelog.model.entities.Log;
import ci.dgmp.sigomap.modulelog.model.entities.LogDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.List;

public interface ILogService
{
    Log logg(String action, Object oldObject, Object newObject, String tableName, Long mainActionId) throws UnknownHostException;

    @Transactional
    Log loggOffConnection(String action, String actorEmail, Object oldObject, Object newObject, String tableName, Long mainActionId) throws UnknownHostException;

    Log saveLog(String action, Long mainActionId) throws UnknownHostException;

    @Transactional
    Log saveLogError(String errorMsg, String stackTrace, Long mainActionId) throws UnknownHostException;

    Log saveLog(String action, String token, Long mainActionId) throws UnknownHostException;

    @Transactional
    Log saveLogOffConnection(String action, String email, Long mainActionId) throws UnknownHostException;

    Log logLoginOrLogout(String username, String action, String connectionId) throws UnknownHostException;

    Log logLoginOrLogout(String username, String action) throws UnknownHostException;

    List<LogDetails> saveLogDetails(Object oldObject, Object newObject, Log log, String tableName, boolean offConnection);

    Page<ConnexionList> getConnextionLogs(String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable);

    Page<ConnexionList> getSystemErrors(String connId, String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable);

    Page<ConnexionList> getConnexionActionLogs(String connId, String key, Long userId, LocalDate debut, LocalDate fin, Pageable pageable);

    void deleteSystemErrors(List<Long> errorIds);
}
