package ci.dgmp.sigomap.authmodule.controller.services.spec;

import java.util.Set;

public interface IAuthoritiesService
{
    Set<String> getAuthorities(Long userId);
}
