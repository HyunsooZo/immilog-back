package com.backend.immilog.global.security;

import com.backend.immilog.global.enums.GlobalCountry;
import com.backend.immilog.global.enums.UserRole;
import com.backend.immilog.user.application.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider implements TokenProvider {
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${token.issuer}")
    private String issuer;
    private SecretKey secretKey;

    @Value("${token.secret-key}")
    private String secretKeyString;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
    }

    private static final long ONE_DAY = 24 * 60 * 60 * 1000L;
    private static final long SIX_MONTH = 6 * 30 * 24 * 60 * 60 * 1000L;

    @Override
    public String issueAccessToken(
            Long id,
            String email,
            UserRole userRole,
            GlobalCountry country
    ) {
        Claims claims =
                Jwts.claims().setSubject(id.toString());
        claims.put("email", email);
        claims.put("userRole", userRole);
        claims.put("country", country.getCountryCode());

        return buildJwt(claims);
    }

    @Override
    public String issueRefreshToken() {
        return buildJwt(null);
    }

    @Override
    public boolean validateToken(
            String token
    ) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    @Override
    public Long getIdFromToken(
            String token
    ) {
        token = removeBearer(token);

        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    @Override
    public String getEmailFromToken(
            String token
    ) {
        token = removeBearer(token);

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("email", String.class);
    }

    @Override
    public Authentication getAuthentication(
            String token
    ) {
        token = removeBearer(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String email = claims.get("email", String.class);
        UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
        authorities.addAll(userRole.getAuthorities()); // 추가된 역할 권한

        return new UsernamePasswordAuthenticationToken(
                userDetails, "", authorities
        );
    }

    @Override
    public UserRole getUserRoleFromToken(
            String authorizationHeader
    ) {
        String token = removeBearer(authorizationHeader);
        return UserRole.valueOf(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userRole", String.class));
    }

    private String removeBearer(
            String token
    ) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    private String buildJwt(
            Claims claims
    ) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() +
                                (claims == null ? SIX_MONTH : ONE_DAY)))
                .signWith(secretKey)
                .compact();
    }

}
