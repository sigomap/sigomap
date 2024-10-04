package ci.dgmp.sigomap.structuremodule.controller.service;

import org.springframework.data.domain.Page;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import ci.dgmp.sigomap.structuremodule.model.dtos.*;
import ci.dgmp.sigomap.structuremodule.model.entities.Structure;
import ci.dgmp.sigomap.typemodule.model.entities.Type;

import java.util.List;

public interface IStrService extends IHierarchySiglesGenerator
{
    ReadStrDTO createStr(CreateStrDTO dto);
    ReadStrDTO updateStr(UpdateStrDTO dto);
    ReadStrDTO deleteStr(Long strId);
    ReadStrDTO restoreStr(Long strId);
    ReadStrDTO changeAncrage(ChangeAnchorDTO dto);

    String generateStrCode(Structure str);

    List<Type> getStrTypes();
    Structure loadChildrenTree(Long strId);

    List<StrTreeView> loadStrTreeView(Long strId);

    List<StrTreeView> loadStrTreeView(Long strId, String critere);

    List<Structure> getParents(Long strId);

    boolean strHasAnyChildMatching(long strId, String key);
    boolean childBelongToParent(Long childId, Long parentId);
    boolean parentHasChild(Long parentId, Long childId);

    Page<ReadStrDTO> searchStr(String key, int pageNum, int pageSize);
    Page<ReadStrDTO> searchStrByType(String key, String typeCode, int pageNum, int pageSize);
    Page<ReadStrDTO> searchStrByParent(String key, Long parentId, int pageNum, int pageSize);

    List<ReadStrDTO> getStrByChildType(String childTypeCode);
    List<SelectOption> getStrAllOptions();
}
