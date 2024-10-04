package ci.dgmp.sigomap.structuremodule.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ci.dgmp.sigomap.authmodule.controller.services.spec.IJwtService;
import ci.dgmp.sigomap.sharedmodule.dtos.SelectOption;
import ci.dgmp.sigomap.sharedmodule.enums.PersStatus;
import ci.dgmp.sigomap.sharedmodule.exceptions.AppException;
import ci.dgmp.sigomap.sharedmodule.utilities.StringUtils;
import ci.dgmp.sigomap.structuremodule.controller.repositories.StrRepo;
import ci.dgmp.sigomap.structuremodule.model.dtos.*;
import ci.dgmp.sigomap.structuremodule.model.entities.Structure;
import ci.dgmp.sigomap.typemodule.controller.repositories.TypeRepo;
import ci.dgmp.sigomap.typemodule.model.entities.Type;
import ci.dgmp.sigomap.typemodule.model.enums.TypeGroup;

import java.util.*;
import java.util.stream.Collectors;

import static ci.dgmp.sigomap.sharedmodule.enums.PersStatus.ACTIVE;

@Service @RequiredArgsConstructor
public class StrService implements IStrService
{
    private final StrRepo strRepo;
    private final StrMapper strMapper;
    private final TypeRepo typeRepo;
    private final HierarchySiglesGenerator hierarchySiglesGenerator;
    private final IJwtService jwtService;

    @Override @Transactional
    public ReadStrDTO createStr(CreateStrDTO dto)
    {
        Structure str = strMapper.mapToStructure(dto);
        if(str.getStrParent() != null)
        {
            if(str.getStrParent().getStrId()==null) str.setStrParent(null);
            else str.setStrLevel(strRepo.getStrLevel(str.getStrParent().getStrId()) + 1);
        }
        str = strRepo.save(str);
        str.setStrCode(this.generateStrCode(str));
        return strMapper.mapToReadStrDTO(str);
    }

    @Override @Transactional
    public ReadStrDTO updateStr(UpdateStrDTO dto)
    {
        Structure str = strMapper.mapToStructure(dto);
        str = strRepo.save(str);
        return strMapper.mapToReadStrDTO(str);
    }

    @Override @Transactional
    public ReadStrDTO deleteStr(Long strId)
    {
        if(strId==null) return null;
        Structure loadedStructure = strRepo.findById(strId).orElse(null);
        if(loadedStructure == null ) return null;
        if(loadedStructure.getStatus() == PersStatus.DELETED) return strMapper.mapToReadStrDTO(loadedStructure);
        loadedStructure.setStatus(PersStatus.DELETED);
        return strMapper.mapToReadStrDTO(loadedStructure);
    }

    @Override @Transactional
    public ReadStrDTO restoreStr(Long strId)
    {
        if(strId==null) return null;
        Structure loadedStructure = strRepo.findById(strId).orElse(null);
        if(loadedStructure == null ) return null;
        if(loadedStructure.getStatus() == ACTIVE) return strMapper.mapToReadStrDTO(loadedStructure);
        loadedStructure.setStatus(ACTIVE);
        return strMapper.mapToReadStrDTO(loadedStructure);
    }

    @Override @Transactional
    public ReadStrDTO changeAncrage(ChangeAnchorDTO dto)
    {
        String actionId = UUID.randomUUID().toString();
        ChangeAnchorDTO dtoBeforeUpdate = strRepo.getChangeAnchorDTO(dto.getStrId());
        long oldStrLevel = dtoBeforeUpdate.getStrLevel();
        Structure str = strMapper.mapToStructure(dto);
        if(dtoBeforeUpdate.equals(dto)) return strMapper.mapToReadStrDTO(str); // Si l'objet n'a pas été modifié, on ne fait aucune action

        String oldStrCode = str.getStrCode();
        str = strRepo.save(str);
        if(!Objects.equals(dtoBeforeUpdate.getNewParentId(), dto.getNewParentId()))
        {
            str.setStrCode(this.generateStrCode(str));
            long newStrLevel = str.getStrLevel();
            Long childrenMaxLevel = strRepo.getChildrenMaxLevel(oldStrCode);
            childrenMaxLevel = childrenMaxLevel == null ? 0L : childrenMaxLevel;
            for(long level = oldStrLevel+ 1; level <= childrenMaxLevel; level++)
            {
                List<Structure> children = strRepo.findChildrenByLevel(oldStrCode, level);
                children.forEach(s->{
                    s.setStrLevel(s.getStrLevel() + (newStrLevel-oldStrLevel));
                    s.setStrCode(this.generateStrCode(s));
                    s = strRepo.save(s);
                });
            }
        }
        return strMapper.mapToReadStrDTO(str);
    }

    @Override
    public String generateStrCode(Structure str)
    {
        if(str == null) throw new AppException("Impossible de généré le code d'une structure non enregistrée");
        if(str.getStrId() == null) throw new AppException("Impossible de généré le code d'une structure non enregistrée");
        String strTypeCode = str.getStrType().getUniqueCode() != null ? str.getStrType().getUniqueCode() : strRepo.getStrTypeUniqueCode(str.getStrId());
        Structure strParent = str.getStrParent() != null ? str.getStrParent() : strRepo.getStrParent(str.getStrId());
        if(strParent == null)
        {
           return strTypeCode + "-" + str.getStrId();
        }
        String parentStrCode = strParent.getStrCode() != null ? strParent.getStrCode() : strRepo.getStrCode(strParent.getStrId());
        return parentStrCode + "/" + strTypeCode + "-" + str.getStrId();
    }

    @Override
    public List<Type> getStrTypes() {
        return typeRepo.findByTypeGroupAndStatus(TypeGroup.TYPE_STR, ACTIVE);
    }

    @Override
    public Structure loadChildrenTree(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  null;
        str.setStrChildren(strRepo.findByStrParent(strId));
        if(str.getStrChildren() == null) return str;
        str.getStrChildren().forEach(s->loadChildrenTree(s.getStrId()));
        return str;
    }

    @Override
    public List<StrTreeView> loadStrTreeView(Long strId, String key)
    {
        if(strId == null) return new ArrayList<>();
        if(!strRepo.existsById(strId)) return new ArrayList<>();
        if(!this.strHasAnyChildMatching(strId, key) && !strRepo.strMatchesSearchKey(strId, key)) return new ArrayList<>();
        Structure str = strRepo.findById(strId).get();
        //str.setStrChildren(strRepo.findByStrParent(strId));
        StrTreeView strTreeView = new StrTreeView();
        strTreeView.setText(str.toString());
        strTreeView.setHref("/sigrh/structures/str-details?tab=str-tree&strId="+str.getStrId());
        List<StrTreeView> childrenNodes = strRepo.getStrChildrenIds(strId).stream().filter(id->this.strHasAnyChildMatching(id, key)).flatMap(childId->this.loadStrTreeView(childId, key).stream()).collect(Collectors.toList());
        strTreeView.setNodes(childrenNodes);
        return Collections.singletonList(strTreeView);
    }

    @Override
    public List<StrTreeView> loadStrTreeView(Long strId)
    {
        if(strId == null) return new ArrayList<>();
        if(!strRepo.existsById(strId)) return new ArrayList<>();
        Structure str = strRepo.findById(strId).get();
        //str.setStrChildren(strRepo.findByStrParent(strId));
        StrTreeView strTreeView = new StrTreeView();
        strTreeView.setText(str.toString());
        strTreeView.setHref("/sigrh/structures/str-details?tab=str-tree&strId="+str.getStrId());
        List<StrTreeView> childrenNodes = strRepo.getStrChildrenIds(strId).stream().flatMap(childId->this.loadStrTreeView(childId).stream()).collect(Collectors.toList());
        strTreeView.setNodes(childrenNodes);
        return Collections.singletonList(strTreeView);
    }

    @Override
    public List<Structure> getParents(Long strId)
    {
        Structure str = strRepo.findById(strId).orElse(null);
        if(str == null) return  new ArrayList<>();
        return strRepo.findAllParents(strId);
    }

    @Override
    public String getHierarchySigles(long strId)
    {
        return hierarchySiglesGenerator.getHierarchySigles(strId);
    }

    @Override
    public boolean strHasAnyChildMatching(long strId, String key)
    {
        if(strRepo.strMatchesSearchKey(strId, key)) return true;
        return strRepo.strHasAnyChildMatching(strRepo.getStrCode(strId), key);
    }

    @Override
    public boolean childBelongToParent(Long childId, Long parentId)
    {
        if(childId == null || parentId == null) return false;
        if(!strRepo.existsById(childId) || !strRepo.existsById(parentId)) return false;
        return strRepo.getStrCode(childId).startsWith(strRepo.getStrCode(parentId) + "/");
    }

    @Override
    public boolean parentHasChild(Long parentId, Long childId)
    {
        return this.childBelongToParent(childId, parentId);
    }

    @Override
    public Page<ReadStrDTO> searchStr(String key, int pageNum, int pageSize)
    {
        Page<Structure> strPage = strRepo.searchStr(key, ACTIVE, PageRequest.of(pageNum, pageSize));
        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), strRepo.countByStatus(ACTIVE));
    }

    @Override
    public Page<ReadStrDTO> searchStrByType(String key, String typeCode, int pageNum, int pageSize)
    {
        Page<Structure> strPage = strRepo.searchStrByType(key, typeCode, ACTIVE, PageRequest.of(pageNum, pageSize));
        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());
        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), strRepo.countByType(typeCode, ACTIVE));
    }

    @Override
    public Page<ReadStrDTO> searchStrByParent(String key, Long parentId, int pageNum, int pageSize)
    {
        if(parentId!= null )
        {
            if (!strRepo.existsById(parentId))
                return new PageImpl<>(new ArrayList<>(), PageRequest.of(pageNum, pageSize), 0);
        }
        Page<Structure> strPage = parentId == null ?
                new PageImpl<>(new ArrayList<>(), PageRequest.of(pageNum, pageSize), 0) :
                strRepo.searchStr(parentId, key==null ? "" : StringUtils.stripAccentsToUpperCase(key.trim()), ACTIVE, PageRequest.of(pageNum, pageSize));
        List<ReadStrDTO> strDTOS = strPage.stream().map(strMapper::mapToReadStrDTO).collect(Collectors.toList());

        return new PageImpl<>(strDTOS, PageRequest.of(pageNum, pageSize), strPage.getTotalElements());
    }

    @Override
    public List<ReadStrDTO> getStrByChildType(String childTypeCode) 
    {
        Long strTutelleId = jwtService.getJwtInfos().getStrId();
        List<Structure> strs = strTutelleId == null ? strRepo.findByChildType(childTypeCode) : strRepo.findByChildType(childTypeCode, strTutelleId);
        return strs.stream().map(str->{ReadStrDTO dto = strMapper.mapToReadStrDTO(str);
            dto.setStrName(str.getStrName() + "(" +this.getParents(str.getStrId()).stream().map(Structure::getStrSigle).reduce("",(s1, s2)->s1+"/"+s2).substring(1) + ")");
            return dto;}).collect(Collectors.toList());
    }

    @Override
    public List<SelectOption> getStrAllOptions()
    {
        List<SelectOption> options = strRepo.getStrAllOptions();
        SelectOption defaultOption = new SelectOption("", "");
        if(options == null) return Collections.singletonList(defaultOption);
        options.add(0, defaultOption);
        return options;
    }
}