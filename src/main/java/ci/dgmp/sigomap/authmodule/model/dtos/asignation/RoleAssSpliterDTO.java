package ci.dgmp.sigomap.authmodule.model.dtos.asignation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RoleAssSpliterDTO
{
    private Set<String> roleCodesToBeRemoved;
    private Set<String> roleCodesToBeAddedAsNew;
    private Set<String> roleCodesToChangeTheDates;
    private Set<String> roleCodesToActivateAndChangeTheDates;
    private Set<String> roleCodesToActivate;
}
