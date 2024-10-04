package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import ci.dgmp.sigomap.authmodule.controller.repositories.*;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class AssMapper {
    @Autowired protected UserRepo userRepo;
    @Autowired protected RoleRepo roleRepo;
    @Autowired protected PrvRepo prvRepo;
    @Autowired protected PrvToRoleAssRepo prvToRoleAssRepo;
    @Autowired protected RoleToFunctionAssRepo roleToFunctionAssRepo;

    public RoleAssSpliterDTO mapToRoleAssSpliterDTO(Set<String> roleCodes, Long principalAssId, LocalDate startsAt, LocalDate endsAt, boolean removable)
    {
        //On l'a mais non présent dans les id envoyés
        Set<String> roleCodesToBeRemoved = !removable ? new HashSet<>() :
                roleToFunctionAssRepo.findActiveRoleCodesBelongingToFnc(principalAssId).stream().filter(id0->roleCodes.stream().noneMatch(id1->id0.equals(id1))).collect(Collectors.toSet()); //

        //On ne l'a pas mais présent dans les id envoyés
        Set<String> roleCodesToBeAddedAsNew = roleToFunctionAssRepo.findRoleCodesNotBelongingToFnc(principalAssId).stream().filter(id0->roleCodes.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il est actif mais les dates ne sont pas identiques (présent dans les id envoyés)
        Set<String> roleCodesToChangeTheDates = roleToFunctionAssRepo.findActiveRoleCodesBelongingToFnc_otherDates(principalAssId, startsAt, endsAt).stream().filter(id0->roleCodes.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il n'est pas actif et les dates ne sont pas identiques (présent dans les id envoyés)
        Set<String> roleCodesToActivateAndChangeTheDates = roleToFunctionAssRepo.findNoneActiveRoleCodesBelongingToFnc_otherDates(principalAssId , startsAt, endsAt).stream().filter(id0->roleCodes.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        //On l'a, il n'est pas actif mais les dates sont pas identiques (présent dans les id envoyés)
        Set<String> roleCodesToActivate = roleToFunctionAssRepo.findNoneActiveRoleCodesBelongingToFnc_sameDates(principalAssId , startsAt, endsAt).stream().filter(id0->roleCodes.stream().anyMatch(id1->id0.equals(id1))).collect(Collectors.toSet());

        return new RoleAssSpliterDTO(roleCodesToBeRemoved, roleCodesToBeAddedAsNew, roleCodesToChangeTheDates, roleCodesToActivateAndChangeTheDates, roleCodesToActivate);
    }

    public RoleAssSpliterDTO mapToRoleAssSpliterDTO(Set<String> roleCodes, Long fncId, LocalDate startsAt, LocalDate endsAt)
    {

        return this.mapToRoleAssSpliterDTO(roleCodes, fncId, startsAt, endsAt, false);
    }

/*
    @Mapping(target = "role", expression = "java(dto.getRoleId() == null ? null : new dgmp.sigrh.auth2.model.entities.AppRole(dto.getRoleId()))")
    @Mapping(target = "privilege", expression = "java(new dgmp.sigrh.auth2.model.entities.AppPrivilege(dto.getPrivilegeId()))")
    @Mapping(target = "ass.startsAt", source = "startsAt")
    @Mapping(target = "ass.endsAt", source = "endsAt")
    public abstract PrvToRoleAss mapToPrvToRoleAss(PrvToRoleDTO dto);



    @Mapping(target = "privilege", expression = "java(prvRepo.findById(dto.getPrivilegeId()).orElse(null))")
    @Mapping(target = "role", expression = "java(roleRepo.findById(dto.getRoleId()).orElse(null))")
    public abstract PrvToRoleAss mapToFullPrvToRoleAss(PrvToRoleDTO dto);


    @Mapping(target = "user", expression = "java(dto.getUserId() == null ? null : new dgmp.sigrh.auth2.model.entities.AppUser(dto.getUserId()))")
    @Mapping(target = "structure", expression = "java(dto.getStrId() == null ? null : new dgmp.sigrh.structuremodule.model.entities.structure.Structure(dto.getStrId()))")
    @Mapping(target = "ass.startsAt", source = "startsAt")
    @Mapping(target = "ass.endsAt", source = "endsAt")
    @Mapping(target = "ass.assStatus", expression = "java(2)")
    public abstract PrincipalAss mapToPrincipalAss(CreatePrincipalAssDTO dto);

    @Mapping(target = "startsAt", source = "ass.startsAt")
    @Mapping(target = "endsAt", source = "ass.endsAt")
    @Mapping(target = "assStatus", source = "ass.assStatus")
    @Mapping(target = "username", source = "ass.user.username")
    @Mapping(target = "strName", source = "ass.structure.strName")
    @Mapping(target = "strSigle", source = "ass.structure.strSigle")
    @Mapping(target = "hierarchySigles", expression = "java(hierarchySiglesGenerator.getHierarchySigles(ass.getStructure().getStrId()))")
    public abstract ReadPrincipalAssDTO mapToReadPrincipalAssDTO(PrincipalAss ass);

    @Mapping(target = "principalAssId", source = "assId")
    public abstract SetAuthoritiesToPrincipalAssDTO mapTo_SetAuthoritiesToPrincipalAssDTO(UpdatePrincipalAssDTO dto);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "strId", source = "structure.strId")
    @Mapping(target = "startsAt", source = "ass.startsAt")
    @Mapping(target = "endsAt", source = "ass.endsAt")
    public abstract UpdatePrincipalAssDTO mapToUpdatePrincipalAssDTO(PrincipalAss ass);



*/
}