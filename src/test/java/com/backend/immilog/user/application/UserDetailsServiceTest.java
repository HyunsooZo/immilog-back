package com.backend.immilog.user.application;

import com.backend.immilog.global.security.UserDetailsServiceImpl;
import com.backend.immilog.user.application.services.query.UserQueryService;
import com.backend.immilog.user.domain.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static com.backend.immilog.global.enums.UserRole.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UserDetailsService 테스트")
class UserDetailsServiceTest {

    private final UserQueryService userQueryService = mock(UserQueryService.class);
    private final UserDetailsService userDetailsService = new UserDetailsServiceImpl(userQueryService);

    @Test
    @DisplayName("유저 정보 가져오기")
    void loadUserByUsername() {
        // given
        String email = "test@email.com";
        User user = User.builder()
                .email(email)
                .password("password")
                .userRole(ROLE_USER)
                .build();

        when(userQueryService.getUserByEmail(email)).thenReturn(Optional.of(user));
        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // then
        assertThat(userDetails).isNotNull();
    }

}