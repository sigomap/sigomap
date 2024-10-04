package ci.dgmp.sigomap.authmodule.controller.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ci.dgmp.sigomap.authmodule.model.entities.Nationalite;

public interface NatRepo extends JpaRepository<Nationalite, String> {
}