package ci.dgmp.sigomap.authmodule.controller.repositories;

import ci.dgmp.sigomap.authmodule.model.entities.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface MenuRepo extends JpaRepository<Menu, Long>
{
    @Query("""
    select (count(m.menuCode)>0) from Menu m where m.menuCode = ?1 and 
    (?2 =  m.prvsCodesChain or 
        locate(concat(?2, '::') , m.prvsCodesChain) = 1 or 
        locate(concat('::', ?2, '::') , m.prvsCodesChain) > 1 or 
        locate(concat('::', ?2), m.prvsCodesChain) = length(m.prvsCodesChain) - length(concat('::', ?2)) + 1) and 
    m.status = 'ACTIVE'
    """)
    boolean menuHasPrivilege(String menuCode, String prvCode);



    @Query("select FUNCTION('string_to_array', m.prvsCodesChain, '::') from Menu m where m.menuCode = ?1")
    String getPrvsCodesByMenuCode(String menuCode);

    @Query("select m from Menu m where m.menuCode = ?1")
    Menu findByMenuCode(String menuCode);

    @Query("select (count(m.menuCode) > 0) from Menu m where m.menuCode = ?1")
    boolean existsByMenuCode(String menuCode);

    @Query("select (count(m.menuCode) > 0) from Menu m where m.name = ?1")
    boolean existsByName(String name);

    @Query("select prv.privilegeCode from AppPrivilege prv where prv.privilegeCode in ?1")
    Set<String> getMenuPrvIdsByMenuCodes(Set<String> menuCodes);

    @Query("SELECT m FROM Menu m WHERE UPPER(cast(FUNCTION('unaccent', m.name) AS string)) LIKE CONCAT('%', UPPER(?1) , '%')")
    Page<Menu> searchMenu2(String key, Pageable pageable);

    @Query("""
SELECT m FROM Menu m WHERE locate(upper(cast(function('unaccent', ?1) as string)) , UPPER(cast(FUNCTION('unaccent', m.name) AS string))) > -1 or 
locate(upper(cast(function('unaccent', ?1) as string)) , UPPER(cast(FUNCTION('unaccent', m.menuCode) AS string))) > -1 

""")
    Page<Menu> searchMenu(String key, Pageable pageable);

    /*@Query("SELECT m FROM Menu m WHERE UPPER(FUNCTION('unaccent', m.name)) LIKE CONCAT('%', UPPER(CAST(?1 AS string)) , '%') OR FUNCTION('unaccent', m.menuCode) LIKE CONCAT('%', UPPER(CAST(?1 AS string)) , '%')")
    Page<Menu> searchMenu(String key, Pageable pageable);*/

}