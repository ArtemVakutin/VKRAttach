package ru.bk.artv.vkrattach.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для осуществления аутентификации по токенам
 */
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationConverter authenticationConverter;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationConverter authenticationConverter) {
        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
    }

    /**
     * Пытается произвести атуентификацию, в случае удачи устанавливает ее в SecurityContext, в случае возникновения ошибок
     * устанавливает в null и продолжает цепь
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Authentication authenticationResult = attemptAuthentication(request, response);
            if (authenticationResult == null) {
                filterChain.doFilter(request, response);
                return;
            }
            successfulAuthentication(request, response, authenticationResult);
            filterChain.doFilter(request, response);
            return;
        } catch (AuthenticationException ex) {
            log.info(ex.getMessage());
            unsuccessfulAuthentication(request, response, filterChain);
            filterChain.doFilter(request, response);
            return;
        }
    }

    /**
     * В случае неуспешной на всякий чистит Security Context от любых аутентификаций
     */
    private void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(null);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
    }

    /**
     * В случае успешной аутентификации устанавливает ее в SecurityContext
     */
    private void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);
    }

    /**
     * converter проверяет на предмет наличия токенов и десеарилизует токен, устанавливая аутентификацию в
     * в PreAuthenticatedAuthenticationToken. Далее AuthenticationManager пытается аутентифицировать уже как
     * TokenUser-а
     */
    private Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = this.authenticationConverter.convert(request);
        if (authentication == null) {
            return null;
        }
        Authentication authenticationResult = authenticationManager.authenticate(authentication);
        if (authenticationResult == null) {
            log.info("Authentication result is {}", "null");
            return null;
        }
        log.info("Authentication result is {}", authenticationResult);

        return authenticationResult;
    }
}

