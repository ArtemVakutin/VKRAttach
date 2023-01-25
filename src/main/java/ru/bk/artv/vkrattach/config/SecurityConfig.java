package ru.bk.artv.vkrattach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.bk.artv.vkrattach.dao.repository.DefaultUserRepository;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(DefaultUserRepository repo) {
        return login -> {
            DefaultUser user = repo.findByLogin((login.toUpperCase()));
            if (user != null) {
                return user;
            }
            throw new UsernameNotFoundException("User : " + login + " not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .antMatchers("/vkrorder", "/getuser")
                .hasRole("USER")
                .antMatchers("/vkradmin", "/getuser")
                .hasRole("ADMIN")
                .antMatchers("/vkrdepartmenthead", "/getuser")
                .hasRole("MODERATOR")
                .antMatchers("/rest/isauthorized")
                .permitAll()
                .antMatchers("/", "/**")
                .permitAll()
//                .hasAnyRole("MODERATOR", "USER", "ADMIN")
//                .anyRequest()
//                .authenticated()
                .and()
                .cors(withDefaults())
                .formLogin()
                .loginProcessingUrl("/process_login")
//                .failureUrl("/index1.html?error=true")
//                .loginPage("/login")
                .and()
                .csrf()
                .disable()
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:3000/" ));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS","PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public String testPass(PasswordEncoder passwordEncoder){
//        String encode = passwordEncoder.encode("11111");
//        System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111");
//        System.out.println(encode);
//        return "111";
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
