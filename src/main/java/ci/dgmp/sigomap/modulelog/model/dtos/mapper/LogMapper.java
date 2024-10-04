package ci.dgmp.sigomap.modulelog.model.dtos.mapper;

import ci.dgmp.sigomap.modulelog.model.dtos.response.ConnexionList;
import ci.dgmp.sigomap.modulelog.model.entities.Log;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LogMapper {
    ConnexionList mapConnexionListToLog(Log log);
}
