package ci.dgmp.sigomap.authmodule.controller.repositories;

import ci.dgmp.sigomap.authmodule.model.entities.AppRole;
import ci.dgmp.sigomap.authmodule.model.entities.RoleToFncAss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RoleToFunctionAssRepo extends JpaRepository<RoleToFncAss, Long>
{
    @Query("select r from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleCode = r.roleCode and roleToFncAss.assStatus = 1 and coalesce(roleToFncAss.startsAt, current_date ) <= current_date and coalesce(roleToFncAss.endsAt, current_date) >= current_date)")
    Set<AppRole> getFncRoles(Long fncId);

    @Query("select r.roleCode from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleCode = r.roleCode and roleToFncAss.assStatus = 1 and coalesce(roleToFncAss.startsAt, current_date ) <= current_date and coalesce(roleToFncAss.endsAt, current_date) >= current_date)")
    Set<String> getFncRoleCodes(Long fncId);

    @Query("select (count(r.roleCode)>0) from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleCode = ?2 and roleToFncAss.assStatus = 1 and current_date between coalesce(roleToFncAss.startsAt, current_date ) and coalesce(roleToFncAss.endsAt, current_date))")
    boolean fncHasRole(Long fncId, String roleCode);

    @Query("select (count(r.roleCode)>0) from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleCode in ?2 and roleToFncAss.assStatus = 1 and current_date between coalesce(roleToFncAss.startsAt, current_date ) and coalesce(roleToFncAss.endsAt, current_date))")
    boolean fncHasAnyRole(Long fncId, List<String> roleCodes);

    @Query("select (count(r.roleCode)>0) from AppRole r where exists(select roleToFncAss from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.role.roleName in ?2 and roleToFncAss.assStatus = 1 and current_date between coalesce(roleToFncAss.startsAt, current_date ) and coalesce(roleToFncAss.endsAt, current_date))")
    boolean fncHasAnyRoleName(Long fncId, List<String> roleNames);

    @Query("select roleToFncAss.role.roleCode from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.assStatus = 1  and roleToFncAss.role.roleCode not in ?2")
    Set<String> findRoleCodesForFncNotIn(Long fncId, Set<String> roleCodesToBeSet);

    @Query("select roleToFncAss.role.roleCode from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.function.fncStatus = 1")
    Set<String> findActiveRoleCodesBelongingToFnc(Long fncId);

    @Query("select roleToFncAss.role.roleCode from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.assStatus = 1 and roleToFncAss.startsAt = ?2 and roleToFncAss.startsAt = ?3")
    Set<String> findActiveRoleCodesBelongingToFnc_sameDates(Long fncId, LocalDate startsAt, LocalDate endsAt);

    @Query("select roleToFncAss.role.roleCode from RoleToFncAss roleToFncAss where roleToFncAss.function.id = ?1 and roleToFncAss.assStatus = 1 and (roleToFncAss.startsAt <> ?2 or roleToFncAss.endsAt <> ?3)")
    Set<String> findActiveRoleCodesBelongingToFnc_otherDates(Long fncId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r.roleCode from AppRole r where not exists( select r2 from RoleToFncAss r2 where r2.function.id = ?1 and r2.role.roleCode = r.roleCode)")
    Set<String> findRoleCodesNotBelongingToFnc(Long fncId);

    @Query("select r.role.roleCode from RoleToFncAss r where r.function.id = ?1 and r.assStatus <> 1")
    Set<String> findNoneActiveRoleCodesBelongingToFnc(Long fncId);

    @Query("select r.role.roleCode from RoleToFncAss r where r.function.id = ?1 and r.function.fncStatus <> 1 and r.function.startsAt = ?2 and r.function.endsAt = ?3")
    Set<String> findNoneActiveRoleCodesBelongingToFnc_sameDates(Long fncId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r.role.roleCode from RoleToFncAss r where r.function.id = ?1 and r.function.fncStatus <> 1 and (r.startsAt <> ?2 or r.endsAt <> ?3)")
    Set<String> findNoneActiveRoleCodesBelongingToFnc_otherDates(Long fncId, LocalDate startsAt, LocalDate endsAt);

    @Query("select r from RoleToFncAss r where r.function.id = ?1 and r.role.roleCode = ?2")
    RoleToFncAss findByFncAndRole(Long fncId, String roleCode);

    @Query("select r from RoleToFncAss r where r.function.id = ?1 and r.assStatus = 1 and current_date between coalesce(r.startsAt, current_date) and coalesce(r.endsAt, current_date)")
    List<RoleToFncAss> findActiveByFnc(Long fncId);

    @Query("select r from RoleToFncAss r where r.function.id = ?1 and locate(?2, cast(function('unaccent', r.role.roleName) as string))>0 and r.assStatus = 1 and current_date between coalesce(r.startsAt, current_date) and coalesce(r.endsAt, current_date)")
    Page<RoleToFncAss> searchActiveByFnc(Long fncId, String key, Pageable pageable);
}