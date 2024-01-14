package ru.bk.artv.vkrattach.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;


/**
 * Создает Рефреш токен. Дает два authority - получение access токена и logout в дополнение к остальным.
 */
public class DefaultRefreshTokenFactory implements RefreshTokenFactory {

    private Duration tokenTtl = Duration.ofDays(1);

    @Override
    public Token createToken(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            var authorities = new LinkedList<String>();
            authorities.add("JWT_REFRESH");
            authorities.add("JWT_LOGOUT");
            authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(authority -> "GRANT_" + authority)
                    .forEach(authorities::add);

            var now = Instant.now();
            return new Token(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(this.tokenTtl), Token.TokenType.REFRESH);
        }
        var now = Instant.now();
        var authorities = new LinkedList<String>();
        authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .forEach(authorities::add);
        return new Token(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(this.tokenTtl), Token.TokenType.REFRESH);
    }
    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }
}
