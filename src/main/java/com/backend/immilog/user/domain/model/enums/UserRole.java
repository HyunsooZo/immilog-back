package com.backend.immilog.user.domain.model.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Getter
public enum UserRole {
    ROLE_USER(
            "ROLE_USER",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
    ),

    ROLE_ADMIN(
            "ROLE_ADMIN",
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
    );

    private final String roleName;
    private final List<GrantedAuthority> authorities;

    UserRole(
            String roleName,
            List<GrantedAuthority> authorities
    ) {
        this.roleName = roleName;
        this.authorities = authorities;
    }

    public boolean isAdmin() {
        return this.equals(UserRole.ROLE_ADMIN);
    }
}