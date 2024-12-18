package com.backend.immilog.user.application.services;

import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.exception.UserException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.backend.immilog.user.exception.UserErrorCode.USER_NOT_FOUND;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserQueryService userQueryService;

    public UserDetailsServiceImpl(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {
        com.backend.immilog.user.domain.model.user.User userDomain = getUser(email);

        List<GrantedAuthority> authorities =
                new ArrayList<>(userDomain.getUserRole().getAuthorities());

        return User.builder()
                .username(userDomain.getEmail())
                .password(userDomain.getPassword())
                .authorities(authorities)
                .build();
    }

    private com.backend.immilog.user.domain.model.user.User getUser(
            String email
    ) {
        return userQueryService
                .getUserByEmail(email)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));
    }
}
