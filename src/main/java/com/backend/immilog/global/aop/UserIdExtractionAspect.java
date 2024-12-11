package com.backend.immilog.global.aop;

import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Aspect
@Component
@RequiredArgsConstructor
public class UserIdExtractionAspect {

    private final TokenProvider tokenProvider;
    private final HttpServletRequest request;

    @Before("@annotation(com.backend.immilog.global.aop.ExtractUserId)")
    public void extractUserDetails() {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Long userSeq = tokenProvider.getIdFromToken(authorizationHeader);
        UserRole userRole = tokenProvider.getUserRoleFromToken(authorizationHeader);

        request.setAttribute("userSeq", userSeq);
        request.setAttribute("userRole", userRole);
    }
}
