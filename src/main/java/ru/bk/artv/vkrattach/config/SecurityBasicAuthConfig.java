package ru.bk.artv.vkrattach.config;

import jakarta.annotation.Priority;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import ru.bk.artv.vkrattach.config.security.JwtLogoutFilter;
import ru.bk.artv.vkrattach.config.security.RefreshTokenFilter;
import ru.bk.artv.vkrattach.config.security.TokenAuthenticationFilter;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.dao.repository.DefaultUserRepository;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;

import java.util.Arrays;
import java.util.List;


/**
 * SecurityFilterChain для Basic аутентификации. При аутентификации пользователю выдается refresh-токен.
 */
@Configuration
//@Priority(1)
public class SecurityBasicAuthConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainBasicAuth(HttpSecurity http,
                                                   SessionAuthenticationStrategy sessionAuthenticationStrategy
    ) throws Exception {

        return http
                .securityMatcher("/process_login")
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sessionConfigurer -> sessionConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(sessionAuthenticationStrategy))
                .addFilterBefore((request, response, filterChain) -> {return;}, AuthorizationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService BasicAuthUserDetailsService(DefaultUserRepository repo) {
        return login -> {
            DefaultUser user = repo.findByLoginIgnoreCase((login.toUpperCase()));
            if (user != null) {
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
                List<SimpleGrantedAuthority> list = Arrays.asList(sga);
                return new TokenUser(user.getLogin(),user.getPassword(), list, null);
            }
            throw new UsernameNotFoundException("User : " + login + " not found");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
