package ru.bk.artv.vkrattach.config.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import ru.bk.artv.vkrattach.config.security.serializers.RefreshTokenJweStringSerializer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * В случае успешной Basic-аутентификации устанавливает аутентификационную refresh-куку.
 */
public class BasicAuthenticationStrategy implements SessionAuthenticationStrategy {

    private RefreshTokenFactory refreshTokenFactory = new DefaultRefreshTokenFactory();

    private RefreshTokenJweStringSerializer refreshTokenStringSerializer;


    public BasicAuthenticationStrategy(RefreshTokenJweStringSerializer refreshTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
    }

    public BasicAuthenticationStrategy(RefreshTokenFactory refreshTokenFactory, RefreshTokenJweStringSerializer refreshTokenStringSerializer) {
        this.refreshTokenFactory = refreshTokenFactory;
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
    }

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request,
                                 HttpServletResponse response) throws SessionAuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            response.setStatus(200);
            var refreshToken = this.refreshTokenFactory.createToken(authentication);
            var refreshTokenString = this.refreshTokenStringSerializer.serialize(refreshToken);
            Instant refreshTokenExpiresAt = refreshToken.expiresAt();

            var cookie = new Cookie("__Token", refreshTokenString);
            cookie.setPath("/jwt");
            cookie.setDomain(null);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), refreshTokenExpiresAt));

            response.addCookie(cookie);
            return;
        }
        throw new AccessDeniedException("User must be authenticated with username/password");
    }

    public void setRefreshTokenFactory(RefreshTokenFactory refreshTokenFactory) {
        this.refreshTokenFactory = refreshTokenFactory;
    }

    public void setRefreshTokenStringSerializer(RefreshTokenJweStringSerializer refreshTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
    }
}
