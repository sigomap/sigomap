package ci.dgmp.sigomap.typemodule.controller.repositories;

import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import ci.dgmp.sigomap.sharedmodule.enums.PersStatus;
import ci.dgmp.sigomap.typemodule.model.dtos.ReadTypeDTO;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TypeRepo extends JpaRepository<Type, String>
{
    @Query("select t from Type t where t.status = ?1")
    List<Type> findByStatus(PersStatus status);

    @Query("select t from Type t where t.status = 'ACTIVE'")
    List<Type> findActiveTypes();

    @Query("select t from Type t where t.typeGroup = ?1 and t.status = ?2")
    List<Type> findByTypeGroupAndStatus(TypeGroup typeGroup, PersStatus status);

    @Query("select t.typeGroup from Type t where t.uniqueCode = ?1")
    TypeGroup findTypeGroupByTypeCode(String typeCode);


    @Query("select t from Type t where (upper(t.name) like upper(concat('%', coalesce(?1, '') , '%')) or upper(t.uniqueCode) like upper(concat('%', coalesce(?1, ''), '%')) or t.typeGroup = ?2) and t.status = ?3")
    Page<Type> searchPageOfTypes(String key, TypeGroup typeGroup, PersStatus status, Pageable pageable);

    @Query("select t from Type t where (upper(t.name) like upper(concat('%', coalesce(?1, ''), '%')) or upper(t.uniqueCode) like upper(concat('%', coalesce(?1, ''), '%'))) and t.typeGroup in ?2 and t.status = ?3")
    Page<Type> searchPageOfTypes(String key, Collection<TypeGroup> typeGroups, PersStatus status, Pageable pageable);

    @Query("select t from Type t where (upper(t.name) like upper(concat('%', coalesce(?1, ''), '%')) or upper(t.uniqueCode) like upper(concat('%', coalesce(?1, ''), '%'))) and t.status = ?2")
    Page<Type> searchPageOfTypes(String key, PersStatus status, Pageable pageable);

    @Query("select t from Type t where t.status = ?1")
    Page<Type> searchPageOfTypes(PersStatus status, Pageable pageable);

    @Query("select new ci.dgmp.sigomap.typemodule.model.dtos.ReadTypeDTO(t.uniqueCode, t.typeGroup, t.name, t.status, t.objectFolder) from Type t where t.typeGroup = ?1 and t.status = 'ACTIVE'")
    List<ReadTypeDTO> findByTypeGroup(TypeGroup typeGroup);

    @Query("select t.uniqueCode from Type t where t.typeGroup = ?1 and t.status = 'ACTIVE' order by t.name")
    List<String> findTypeCodesByTypeGroup(TypeGroup typeGroup);

    @Query("select new ci.dgmp.sigomap.typemodule.model.dtos.ReadTypeDTO( t.uniqueCode, t.typeGroup, t.name, t.status) from Type t order by t.typeGroup, t.uniqueCode, t.objectFolder")
    List<ReadTypeDTO> findAllTypes();

    @Query("select new ci.dgmp.sigomap.typemodule.model.dtos.ReadTypeDTO(s.child.uniqueCode, s.child.typeGroup, s.child.name, s.child.status) from TypeParam s where s.parent.uniqueCode = ?1 order by s.child.name")
    List<ReadTypeDTO> findSousTypeOf(String uniqueCode);

    @Query("select tp.child from TypeParam  tp where tp.parent.uniqueCode = ?1 and tp.status = 'ACTIVE' and tp.child.status ='ACTIVE'")
    List<Type> findActiveSousTypes(String parentCode);

    @Query("select tp.child.uniqueCode from TypeParam  tp where tp.parent.uniqueCode = ?1 and tp.child.status = 'ACTIVE' and tp.status = 'ACTIVE'")
    List<String> findChildrenCodes(String parentCode);

    @Query("select (count (stp)>0) from TypeParam stp where stp.child.uniqueCode=?2 and stp.parent.uniqueCode=?1")
    boolean isSousTypeOf(String parentCode, String childCode);

    @Query("select (count(t) > 0) from Type t where upper(t.uniqueCode) = upper(?1)")
    boolean existsByUniqueCode(String uniqueCode);

    @Query("select (count(t) > 0) from Type t where t.typeGroup = ?1 and upper(t.uniqueCode) = upper(?2)")
    boolean existsByGroupAndUniqueCode(TypeGroup valueOf, String uniqueCode);

    @Query("select (count(stp) = 0) from TypeParam stp where stp.child.uniqueCode = ?1 or stp.parent.uniqueCode = ?1")
    boolean isDeletable(String uniqueCode);

    @Modifying
    @Query("delete from Type t where t.uniqueCode = ?1")
    long deleteByUniqueCode(String uniqueCode);

    @Modifying
    @Query("update Type t set t.typeGroup = :typeGroup, t.name = :name where t.uniqueCode =:uniqueCode")
    long updateType(@Param("uniqueCode") String uniqueCode, @Param("typeGroup") String typeGroup, @Param("name") String name);

    @Query("select (count(t)>0) from Type t where t.typeGroup = ?1 and t.uniqueCode = ?2 and t.status = 'ACTIVE'")
    boolean typeGroupHasChild(TypeGroup typeGroup, String uniqueCode);

    @Query("select t.objectFolder from Type t where t.uniqueCode = ?1")
    String getObjectFolderByUniqueCode(String uniqueCode);

    @Query("select t.uniqueCode from Type t where t.objectFolder = ?1")
    List<String> findUniqueCodesByObjectFolder(String objectFolder);

    @Query("select (count(t.typeParamId)>0) from TypeParam t where t.child.uniqueCode = ?1")
    boolean typeHasAnyParent(String uniqueCode);

    @Query("select (count(t.typeParamId)>0) from TypeParam t where t.child.uniqueCode = ?1 and t.child.typeGroup = ?2")
    boolean typeHasAnyParent(String uniqueCode, TypeGroup typeGroup);

    @Query("select t from Type t where t.typeGroup = ?1 and t.status = 'ACTIVE' and (select count(tp.typeParamId) from TypeParam tp where tp.child.uniqueCode = t.uniqueCode and tp.status = 'ACTIVE') = 0")
    List<Type> findBaseTypes(TypeGroup typeGroup);

    @Query("select tp.child from TypeParam tp where tp.parent.uniqueCode = ?1 and tp.status = 'ACTIVE'")
    List<Type> getChildren(String uniqueCode);


    @Query("select (count(t.uniqueCode)>0) from Type t where t.name = ?1")
    boolean existsByName(String name);

    @Query("select (count(t.uniqueCode)>0) from Type t where t.name = ?1 and t.uniqueCode <> ?2")
    boolean existsByName(String name, String uniqueCode);

    @Query("select (count(t.uniqueCode)>0) from Type t where t.uniqueCode = ?1 and exists (select t1 from Type t1 where t1.uniqueCode = ?1 and t1.uniqueCode <> ?2)")
    boolean existsByUniqueCode(String uniqueCode, String oldUniqueCode);

    @Query("select new ci.dgmp.sigomap.sharedmodule.dtos.SelectOption(t.uniqueCode, t.name) from Type t where t.typeGroup = ?1 and t.status = 'ACTIVE'")
    List<SelectOption> findOptionsByTypeGroup(TypeGroup typeGroup);
}
