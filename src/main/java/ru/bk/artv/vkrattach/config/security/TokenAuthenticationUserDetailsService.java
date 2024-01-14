package ru.bk.artv.vkrattach.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.bk.artv.vkrattach.dao.repository.DeactivatedTokenRepository;

import java.time.Instant;

/**
 * Для преобразования TOKEN-аутентификации в обычную с соответствующими правами доступа.
 * Access токен преобразуется напрямую, refresh проверяется по базе данных, нет ли его в заблокированных
 */
@Slf4j
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {


    private final DeactivatedTokenRepository deactivatedTokenRepository;

    public TokenAuthenticationUserDetailsService(DeactivatedTokenRepository deactivatedTokenRepository) {
        this.deactivatedTokenRepository = deactivatedTokenRepository;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {

        if (authenticationToken.getPrincipal() instanceof Token token) {
            if (token.tokenType().equals(Token.TokenType.ACCESS)) {
                return new TokenUser(token.subject(), "nopassword", true, true,
                                token.expiresAt().isAfter(Instant.now()),
                        true,
                        token.authorities().stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList(), token);
            }
            return new TokenUser(token.subject(), "nopassword", true, true,
                    !deactivatedTokenRepository.existsById(token.id()) &&
                            token.expiresAt().isAfter(Instant.now()),
                    true,
                    token.authorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList(), token);
        }

        throw new UsernameNotFoundException("Principal must me of type Token");
    }
}
