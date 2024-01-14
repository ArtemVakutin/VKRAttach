package ru.bk.artv.vkrattach.config.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.bk.artv.vkrattach.config.security.serializers.AccessTokenJwsStringDeserializer;
import ru.bk.artv.vkrattach.config.security.serializers.RefreshTokenJweStringDeserializer;

import java.util.stream.Stream;


/**
 * Конфертирует JWT токены в аутентификацию. Сперва access, если не находит, то refresh. Иначе возвращает Null.
 * Refresh токены проверяет на deactivated
 */
@Slf4j
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/jwt/**", HttpMethod.POST.name());
    private final AccessTokenJwsStringDeserializer accessTokenStringDeserializer;

    private final RefreshTokenJweStringDeserializer refreshTokenStringDeserializer;

    public JwtAuthenticationConverter(AccessTokenJwsStringDeserializer accessTokenStringDeserializer,
                                      RefreshTokenJweStringDeserializer refreshTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        this.refreshTokenStringDeserializer = refreshTokenStringDeserializer;
    }

    /*
    В случае наличия Access токена производит авторизацию по нему. В случае наличия RefreshToken и пути
    обновления /jwt/refresh производит авторизацию по refresh-токену для обновления обоих токенов
     */

    @Override
    public Authentication convert(HttpServletRequest request) {
        log.trace("Starting token authentification with JwtAuthenticationConverter");
        log.trace("Http request matches /jwt : {}", requestMatcher.matches(request));
        log.trace("Request url is : {}", request.getRequestURL());

        var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization != null && authorization.startsWith("Bearer ") && !requestMatcher.matches(request)) {
            log.trace("Converter found access token, starting deserializing");

            var token = authorization.replace("Bearer ", "");
            var accessToken = this.accessTokenStringDeserializer.deserialize(token);
            if (accessToken != null) {
                return new PreAuthenticatedAuthenticationToken(accessToken, token);
            }
        }

        if (request.getCookies() != null && requestMatcher.matches(request)) {
            log.trace("Converter found cookies & request matches /jwt, starting deserializing");
            return Stream.of(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("__Token"))
                    .findFirst()
                    .map(cookie -> {
                        String str;
                        if ((str = cookie.getValue()).equals("")) {
                            return null;
                        }
                        var refreshToken = this.refreshTokenStringDeserializer.deserialize(str);
                        return new PreAuthenticatedAuthenticationToken(refreshToken, cookie.getValue());
                    })
                    .orElse(null);
        }

        return null;
    }
}
