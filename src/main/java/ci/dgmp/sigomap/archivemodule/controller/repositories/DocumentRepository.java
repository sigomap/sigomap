package ci.dgmp.sigomap.archivemodule.controller.repositories;

import ci.dgmp.sigomap.archivemodule.model.dtos.response.ReadDocDTO;
import ci.dgmp.sigomap.archivemodule.model.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long>
{
    @Query("select d.docPath from Document d where d.docId = ?1")
    String getDocumentPath(Long docId);

    @Query("""
        select new ci.dgmp.sigomap.archivemodule.model.dtos.response.ReadDocDTO(
        d.docId, d.docNum, d.docName, d.docDescription, d.docPath, d.docType.uniqueCode, d.docType.name) 
        from Document d left join d.user u 
        where (
        locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(d.docDescription, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(d.docType.name, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(d.docNum, '') ) as string))) >0 
        or locate(upper(coalesce(:key, '')), upper(cast(function('unaccent',  coalesce(d.docName, '') ) as string))) >0
        )
        and
        (u.userId is null or u.userId = :userId)
    """)
    Page<ReadDocDTO> getAllDocsForObject(@Param("userId") Long userId,  @Param("key") String key, Pageable pageable);
}