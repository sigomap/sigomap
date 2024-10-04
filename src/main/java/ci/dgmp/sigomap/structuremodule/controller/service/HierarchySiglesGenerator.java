package ci.dgmp.sigomap.structuremodule.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ci.dgmp.sigomap.structuremodule.controller.repositories.StrRepo;

@Service @RequiredArgsConstructor
public class HierarchySiglesGenerator implements IHierarchySiglesGenerator
{
    private final StrRepo strRepo;
    @Override
    public String getHierarchySigles(long strId)
    {
        if(!strRepo.existsById(strId)) return "/";
        String hs = strRepo.getHierarchySigles(strId).stream().reduce("", (s1, s2)->s1 + "/" + s2).substring(1);
        if(hs!=null && hs.startsWith("//")) return hs.substring(1);
        return hs==null ? "/" : hs;
    }
}
