package ci.dgmp.sigomap.modulestatut.repositories;

import ci.dgmp.sigomap.modulestatut.entities.Statut;
import ci.dgmp.sigomap.sharedmodule.enums.TypeStatut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatutRepository extends JpaRepository<Statut, String>
{
    @Query("select s from Statut s where s.staCode = ?1")
    Statut findByStaCode(String staCode);

    @Query("select (count(s) > 0) from Statut s where s.staCode = ?1")
    boolean alreadyExistsByCode(String statCode);

    @Query("select (count(s)>0) from Statut s where s.staLibelle = ?1 and s.staType = ?2")
    boolean alreadyExistsByLibelleAndType(String libelle, TypeStatut type);


    @Query("select (count(s)>0) from Statut s where s.staLibelle = ?1 and s.staType = ?2 and s.staCode <>?3")
    boolean alreadyExistsByLibelleAndType(String libelle, TypeStatut type, String staCode);

    @Query("select s.staCode from Statut s where s.staType = ?1")
    List<String> getStaCodesByTypeStatut(TypeStatut typeStatut);
}
