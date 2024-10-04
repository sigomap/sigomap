package ci.dgmp.sigomap.authmodule.controller.repositories;

import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.PrvByTypeDTO;
import ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO;
import ci.dgmp.sigomap.authmodule.model.entities.AppPrivilege;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PrvRepo extends JpaRepository<AppPrivilege, String>
{
    @Query("select (count(p.privilegeCode)>0) from AppPrivilege p where upper(p.privilegeCode) = upper(?1) ")
    boolean existsByCode(String prvCode);


    @Query("select (count(p.privilegeCode)>0) from AppPrivilege p where upper(p.privilegeName) = upper(?1)")
    boolean existsByName(String name);

    @Query("select (count(p.privilegeCode)>0) from AppPrivilege p where upper(p.privilegeName) = upper(?1) and p.privilegeCode <> ?2")
    boolean existsByName(String name, String prvCode);

    @Query("SELECT prv FROM AppPrivilege prv WHERE " +
            "locate(upper(coalesce(:searchKey, '')) , upper(CAST(FUNCTION('unaccent', prv.privilegeCode) AS string )) ) > 0 OR " +
            "locate(upper(coalesce(:searchKey, '')), upper(CAST(FUNCTION('unaccent', prv.privilegeName) as string))) > 0" +
            "order by prv.updatedAt desc, prv.createdAt desc, prv.privilegeCode asc")
    Set<AppPrivilege> searchPrivileges(@Param("searchKey") String searchKey);

    @Query("SELECT prv FROM AppPrivilege prv WHERE " +
            "locate(upper(coalesce(:searchKey, '') ) , upper(CAST(FUNCTION('unaccent', prv.privilegeCode) AS string )) ) > 0 OR " +
            "locate(upper(coalesce(:searchKey, '')), upper(CAST(FUNCTION('unaccent', prv.privilegeName) as string))) > 0" +
            "order by prv.updatedAt desc, prv.createdAt desc, prv.privilegeCode asc")
    Page<AppPrivilege> searchPrivileges(@Param("searchKey") String searchKey, Pageable pageable);

    @Query("""
            SELECT prv FROM AppPrivilege prv WHERE prv.prvType.uniqueCode in :typePrvUniqueCodes and
            (locate(upper(coalesce(:searchKey, '') ) , upper(CAST(FUNCTION('unaccent', prv.privilegeCode) AS string )) ) > 0 OR
            locate(upper(coalesce(:searchKey, '')), upper(CAST(FUNCTION('unaccent', prv.privilegeName) as string))) > 0)
            """)
    Page<AppPrivilege> searchPrivileges(@Param("searchKey") String searchKey, @Param("typePrvUniqueCodes") List<String> typePrvUniqueCodes, Pageable pageable);

    @Query("""
    select new ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO
    (
        prv0.privilegeCode, prv0.privilegeName, prv0.prvType.name
    ) from AppPrivilege  prv0 where 
    (
     prv0.privilegeCode in 
        (select ptr.privilege.privilegeCode from PrvToRoleAss ptr where ptr.assStatus = 1 and ptr.role.roleCode in 
            (select rtf.role.roleCode from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 
            and current_date between coalesce(rtf.startsAt, current_date) and coalesce(rtf.endsAt, current_date))
        ) ) 
    """)
    List<ReadPrvDTO> getFunctionPrvs(Long fctId);

    @Query("""
    select prv.privilegeCode from AppPrivilege prv where prv.privilegeCode in 
        (select ptr.privilege.privilegeCode from PrvToRoleAss ptr where ptr.assStatus = 1 and ptr.role.roleCode in 
            (select rtf.role.roleCode from RoleToFncAss rtf where rtf.function.id = ?1 and rtf.assStatus = 1 
            and current_date between coalesce(rtf.startsAt, current_date) and coalesce(rtf.endsAt, current_date))
        )
    """)
    Set<String> getFunctionPrvCodes(Long fctId);

    @Query("""
            select new ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO
            (
                p.privilegeCode, p.privilegeName, p.prvType.name
            ) from AppPrivilege p where p.prvType.uniqueCode = ?1
             """)
    Set<ReadPrvDTO> getTypePriveleges(String typeCode);

    @Query("""
            select new ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.PrvByTypeDTO
            (
                p.prvType.name, p.prvType.uniqueCode, 
                (select new ci.dgmp.sigomap.authmodule.model.dtos.appprivilege.ReadPrvDTO(p1.privilegeCode, p1.privilegeCode, p1.privilegeName, p1.prvType.name) from AppPrivilege p1 where p1.prvType.uniqueCode = ?1)
            ) 
            from AppPrivilege p where p.prvType.uniqueCode = ?1
             """)
    Set<PrvByTypeDTO> getPrvByTypeDTOS(String typeCode);

    @Query("select p.privilegeCode from AppPrivilege p where p.privilegeCode in ?1")
    Set<String> getPrvCodesByPrvIds(Set<Long> prvIds);

    @Query("select p from AppPrivilege p where p.privilegeCode in ?1")
    List<AppPrivilege> findByPrvCodes(List<String> asList);

    @Query("select new ci.dgmp.sigomap.sharedmodule.dtos.SelectOption(p.privilegeCode, p.privilegeName, p.prvType.name) from AppPrivilege p where p.prvType.uniqueCode in ?1")
    List<SelectOption> getPrivilegesByTypes(List<String> typeCodes);

    @Query("select new ci.dgmp.sigomap.sharedmodule.dtos.SelectOption(p.privilegeCode, p.privilegeName, p.prvType.name) from AppPrivilege p")
    List<SelectOption> getAllOptions();
}