package ru.bk.artv.vkrattach.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.bk.artv.vkrattach.config.security.serializers.AccessTokenJwsStringSerializer;
import ru.bk.artv.vkrattach.config.security.serializers.AccessTokenJwsStringSerializerImpl;
import ru.bk.artv.vkrattach.config.security.serializers.RefreshTokenJweStringSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;


/**
 * Фильтр предоставляет refresh и access токены по refresh токену. До этого фильтра клиент должен быть авторизирован
 * по Refresh - токену с правами "JWT_REFRESH"
 */
@Slf4j
public class RefreshTokenFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/refresh", HttpMethod.POST.name());

    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    private RefreshTokenFactory refreshTokenFactory = new DefaultRefreshTokenFactory();

    private AccessTokenFactory accessTokenFactory = new DefaultAccessTokenFactory();

    private RefreshTokenJweStringSerializer refreshTokenStringSerializer;

    private AccessTokenJwsStringSerializer accessTokenStringSerializer;

    private ObjectMapper objectMapper = new ObjectMapper();

    public RefreshTokenFilter(RefreshTokenJweStringSerializer refreshTokenStringSerializer, AccessTokenJwsStringSerializer accessTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
        this.accessTokenStringSerializer = accessTokenStringSerializer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                        context.getAuthentication().getPrincipal() instanceof TokenUser &&
                        context.getAuthentication().getAuthorities()
                                .contains(new SimpleGrantedAuthority("JWT_REFRESH"))) {
                    TokenUser user = (TokenUser)context.getAuthentication().getPrincipal();
                    var refreshToken = this.refreshTokenFactory.createToken(context.getAuthentication());

                    var refreshTokenString = this.refreshTokenStringSerializer.serialize(refreshToken);
                    Instant refreshTokenExpiresAt = refreshToken.expiresAt();

                    var cookie = new Cookie("__Token", refreshTokenString);
                    cookie.setPath("/jwt");
                    cookie.setDomain(null);
                    cookie.setSecure(false);
                    cookie.setHttpOnly(true);
                    cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), refreshTokenExpiresAt));

                    response.addCookie(cookie);

                    var accessToken = this.accessTokenFactory.createToken(user.getToken());

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    this.objectMapper.writeValue(response.getWriter(),
                            new TokenData(this.accessTokenStringSerializer.serialize(accessToken),
                                    accessToken.expiresAt().toString()));
                    return;
                }
                throw new AccessDeniedException("User must be authenticated with JWT");
            }

            throw new AccessDeniedException("User must be authenticated with JWT");
        }

        filterChain.doFilter(request, response);
    }

    public void setRequestMatcher(RequestMatcher requestMatcher) {
        this.requestMatcher = requestMatcher;
    }

    public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
        this.securityContextRepository = securityContextRepository;
    }

    public void setRefreshTokenFactory(RefreshTokenFactory refreshTokenFactory) {
        this.refreshTokenFactory = refreshTokenFactory;
    }

    public void setAccessTokenFactory(AccessTokenFactory accessTokenFactory) {
        this.accessTokenFactory = accessTokenFactory;
    }

    public void setRefreshTokenStringSerializer(RefreshTokenJweStringSerializer refreshTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
    }

    public void setAccessTokenStringSerializer(AccessTokenJwsStringSerializerImpl accessTokenStringSerializer) {
        this.accessTokenStringSerializer = accessTokenStringSerializer;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
