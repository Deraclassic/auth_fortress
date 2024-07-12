package com.dera.AuthFortress.service.impl;

import com.dera.AuthFortress.domain.entities.AccessToken;
import com.dera.AuthFortress.infrastructure.config.JwtService;
import com.dera.AuthFortress.repository.AccessTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {
    private final AccessTokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Invalid Authorization header");
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByLoginToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.getLoginToken();
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
