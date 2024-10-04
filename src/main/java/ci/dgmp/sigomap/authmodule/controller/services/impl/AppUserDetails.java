package ci.dgmp.sigomap.authmodule.controller.services.impl;

import ci.dgmp.sigomap.authmodule.controller.services.spec.IAuthoritiesService;
import ci.dgmp.sigomap.authmodule.model.entities.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AppUserDetails implements UserDetails
{
    private final AppUser user;
    private final IAuthoritiesService authoritiesService;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authoritiesService.getAuthorities(this.user.getUserId()).stream().map(auth->new SimpleGrantedAuthority(auth)).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isNotBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}