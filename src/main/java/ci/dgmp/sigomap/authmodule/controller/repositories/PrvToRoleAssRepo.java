package ci.dgmp.sigomap.authmodule.controller.repositories;

import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import ci.dgmp.sigomap.authmodule.model.entities.AppPrivilege;
import ci.dgmp.sigomap.authmodule.model.entities.PrvToRoleAss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface PrvToRoleAssRepo extends JpaRepository<PrvToRoleAss, Long>
{
    @Query("select (count(p) > 0) from PrvToRoleAss p where p.role.roleCode = ?1 and p.privilege.privilegeCode = ?2")
    boolean existsByRoleAndPrivilege(String roleCode, String privilegeCode);

    @Query("select (count(p) > 0) from PrvToRoleAss p where p.role.roleCode = ?1 and p.privilege.privilegeCode = ?2 and p.assStatus = ?3")
    boolean existsByRoleAndPrivilege(String roleCode, String privilegeCode, int assStatus);

    @Query("select p from PrvToRoleAss p where p.role.roleCode = ?1 and p.privilege.privilegeCode = ?2")
    PrvToRoleAss findByRoleAndPrivilege(String roleCode, String prvCode);

    @Query("select p from PrvToRoleAss p where p.role.roleCode = ?1")
    List<PrvToRoleAss> findByRole(String roleCode);

    @Query("select p.privilege.privilegeCode from PrvToRoleAss p where p.role.roleCode = ?1 and p.assStatus = 1  and coalesce(p.startsAt, current_date ) <= current_date and coalesce(p.endsAt, current_date) >= current_date")
    Set<String> findActivePrvCodesForRole(String roleCode);

    @Query("select p.privilege.privilegeCode from PrvToRoleAss p where p.role.roleCode in ?1 and p.assStatus = 1  and current_date between coalesce(p.startsAt, current_date ) and coalesce(p.endsAt, current_date)")
    Set<String> findActivePrvCodesForRoles(Set<String> roleCodes);

    @Query("""
    select new ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO(
        p.privilege.privilegeCode, p.privilege.privilegeName, p.privilege.prvType.name )
    from PrvToRoleAss p where p.role.roleCode in ?1 and p.assStatus = 1  and current_date between coalesce(p.startsAt, current_date ) and coalesce(p.endsAt, current_date)
    """)
    Set<ReadPrvDTO> findActivePrivilegesForRoles(Set<String> roleCodes);

    @Query("select p.privilege.privilegeCode from PrvToRoleAss p where p.role.roleCode = ?1 and p.assStatus = 1 and current_date between coalesce(p.startsAt, current_date) and coalesce(p.endsAt, current_date) and p.privilege.privilegeCode not in ?2")
    Set<String> findPrvCodesForRoleNotIn(String roleCode, Set<String> prvCodesToBeSet);

    //@Query("select p.privilege.privilegeCode from PrvToRoleAss p where (p.role.roleCode <> ?1 or (p.role.roleCode = ?1 and (p.ass.assStatus <>1 or coalesce(p.ass.startsAt, current_date) > current_date or coalesce(p.ass.endsAt, current_date) < current_date ))) and p.privilege.privilegeCode in ?2")
    //Set<String> findPrvCodesNotBelongingToRoleIn(String roleCode, Set<String> prvCodesToBeSet);

    @Query("select p.privilegeCode from AppPrivilege p where p.privilegeCode in ?2 and not exists (select ptr.assId from PrvToRoleAss ptr where ptr.role.roleCode = ?1 and ptr.privilege.privilegeCode = p.privilegeCode and current_date between coalesce(ptr.startsAt, current_date) and coalesce(ptr.endsAt, current_date))")
    Set<String> findPrvCodesNotBelongingToRoleIn(String roleCode, Set<String> prvCodes);

    @Query("select p.privilege.privilegeCode from PrvToRoleAss p where p.role.roleCode = ?1 and p.assStatus = 1 and p.privilege.privilegeCode in ?2 and coalesce(p.startsAt, ?3) = ?3 and coalesce(p.endsAt, ?4) = ?4 and coalesce(p.startsAt, current_date ) <= current_date and coalesce(p.endsAt, current_date) >= current_date")
    Set<String> findActivePrvCodesForRoleIn_sameDates(String roleCode, Set<String> prvCodesToBeSet, LocalDate startsAt, LocalDate endsAt);

    @Query("select p.privilege.privilegeCode from PrvToRoleAss p where p.role.roleCode = :roleCode and p.assStatus = 1 and p.privilege.privilegeCode in :prvCodesToBeSet and( p.startsAt <> :startsAt or p.endsAt <> :endsAt)")
    Set<String> findActivePrvCodesForRoleIn_otherDates(@Param("roleCode") String roleCode, @Param("prvCodesToBeSet")Set<String> prvCodesToBeSet, @Param("startsAt")LocalDate startsAt, @Param("endsAt") LocalDate endsAt);

    @Query("select p.privilege.privilegeCode from PrvToRoleAss p where p.role.roleCode = ?1 and p.assStatus <> 1 and p.privilege.privilegeCode in ?2")
    Set<String> findNoneActivePrvCodesForRoleIn(String roleCode, Set<String> prvCodesToBeSet);

    @Query("select p from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleCode = ?1 and ptrAss.privilege.privilegeCode = p.privilegeCode and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)")
    Set<AppPrivilege> getRolePrivileges(String roleCode);

    @Query("select (count(a.assId) > 0) from PrvToRoleAss a where a.role.roleCode = ?1 and a.privilege.privilegeCode = ?2 and a.assStatus = 1 and current_date between coalesce(a.startsAt, current_date ) and coalesce(a.endsAt, current_date)")
    boolean roleHasValidPrivilege(String roleCode, String privilegeCode);

    @Query("select (count(p.privilegeCode)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleCode = ?1 and ptrAss.privilege.privilegeCode = ?2 and ptrAss.assStatus = 1 and current_date between coalesce(ptrAss.startsAt, current_date ) and coalesce(ptrAss.endsAt, current_date))")
    boolean roleHasPrivilege(String roleCode, String prvCode);

    @Query("select (count(p.privilegeCode)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.role.roleCode = ?1 and ptrAss.privilege.privilegeCode in ?2 and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)")
    boolean roleHasAnyPrivilege(String roleCode, List<String> prvCodes);

    @Query("select (count(p.privilegeCode)>0) from AppPrivilege p where exists (select ptrAss from PrvToRoleAss ptrAss where ptrAss.privilege.privilegeCode = ?1 and ptrAss.role.roleCode in ?2 and ptrAss.assStatus = 1 and coalesce(ptrAss.startsAt, current_date ) <= current_date and coalesce(ptrAss.endsAt, current_date) >= current_date)")
    boolean prvBelongToAnyRole(String prvCode, Set<String> roleCodes);
}