package ci.dgmp.sigomap.modulelog.controller.repositories;

import ci.dgmp.sigomap.modulelog.model.entities.LogDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDetailsRepo extends JpaRepository<LogDetails, Long> {
}
