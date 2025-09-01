package com.healthcare.finder.doctorHospitalFinder.application.wrapper;

import com.healthcare.finder.doctorHospitalFinder.application.entity.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetail implements UserDetails {
    private final AppUser appUser;

    public CustomUserDetail(AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(appUser.getRole().getRoleName()));
    }

    @Override
    public String getPassword() {
        return appUser.getUserPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getUserEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
