package ci.dgmp.sigomap.authmodule.controller.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO;
import ci.dgmp.sigomap.authmodule.model.entities.AppFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface FunctionRepo extends JpaRepository<AppFunction, Long>
{
    @Query("select p from AppFunction p where p.user.userId= ?1 order by p.fncStatus asc")
    Set<AppFunction> findAllByUser(Long userId);

    @Query("select (count(f.id)>0) from AppFunction f where f.user.userId = ?1 and upper(f.name) = upper(?2) ")
    boolean existsForUserByUserAndName(long userId, String functionName);

    //On cherche à savoir s'il existe une autre fonction pour l'utilisateur de la fonction dont l'id est en paramètre qui porte déjà le même nom
    @Query("select (count(f.id)>0) from AppFunction f where f.user.userId = (select f1.user.userId from AppFunction f1 where f1.id = ?1) and upper(f.name) = upper(?2) and f.id <> ?1")
    boolean existsForUserByFncAndName(long fncId, String functionName);
    //On prend les fonctions de status 1,2,3 si withRevoked est à true sinon on ne prend que ceux avec le status 1,2
    @Query("""
        select f from AppFunction f left join f.type t 
        where f.user.userId= :userId and 
        (locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(f.name, '') ) as string))) >0 
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(t.name, '') ) as string) )) >0
           or locate(upper(coalesce(:key, '') ), upper(cast(function('unaccent',  coalesce(t.uniqueCode) ) as string) )) >0)
           and (f.fncStatus in :fncStatus)
        order by f.fncStatus asc
    """)
    Page<AppFunction> findAllByUser(@Param("userId") Long userId, @Param("key") String key, @Param("fncStatus")int[] fncStatus, Pageable pageable);

    @Query("select p from AppFunction p where p.user.userId= ?1 and p.fncStatus in (1, 2) and current_date between coalesce(p.startsAt, current_date ) and coalesce(p.endsAt, current_date)") //coalesce(p.ass.startsAt, current_date ) <= current_date and coalesce(p.ass.endsAt, current_date) >= current_date
    Set<AppFunction> findNoneRevokedByUser(Long userId);

    @Query("select p from AppFunction p where p.user.userId= ?1 and (p.fncStatus = 1 or p.fncStatus = 2) order by p.fncStatus asc")
    List<AppFunction> findActiveByUser(Long userId);

    @Query("select f.id from AppFunction f where f.user.userId= ?1 and f.fncStatus = 1 ")
    List<Long> getCurrentFncIds(long userId);

    @Query("select f from AppFunction f where f.user.userId= ?1 and f.fncStatus = 1 ")
    List<AppFunction> getCurrentFunctions(long userId);

    @Query("select f.name from AppFunction f where f.user.userId= ?1 and f.fncStatus = 1 and coalesce(f.startsAt, current_date ) <= current_date and coalesce(f.endsAt, current_date) >= current_date")
    Set<String> getCurrentFncNames(Long userId);

    @Query("select (count(ps)>0) from AppFunction ps where ps.user.userId = ?1")
    boolean userHasAnyAppFunction(Long userId);

    @Query("select (count(ps)>0) from AppFunction ps where ps.user.email = ?1")
    boolean userHasAnyAppFunction(String username);

    @Query("select f.user.userId from AppFunction f where f.id = ?1")
    Long getUserId(Long id);

    @Query("select (count(u.userId) = 1) from AppFunction f join f.user u where u.currentFunctionId = ?1 and u.userId = ?2 and f.fncStatus = 1 and current_date between coalesce(f.startsAt, current_date) and coalesce(f.endsAt, current_date)")
    boolean functionIsCurrentForUser(Long fncId, Long userId);

    @Query("""
    select new ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO(f.id, f.type.uniqueCode, f.name, f.startsAt, f.endsAt) 
    from AppFunction f where f.user.userId = ?1
""")
    List<UpdateFncDTO> getUpdateFncDTOSByUserId(Long userId);

    @Query("""
    select new ci.dgmp.sigomap.authmodule.model.dtos.appfunction.UpdateFncDTO(f.id, f.type.uniqueCode, f.name, f.startsAt, f.endsAt) 
    from AppFunction f where f.id = ?1
""")
    UpdateFncDTO getUpdateFncDTO(Long fncId);
}