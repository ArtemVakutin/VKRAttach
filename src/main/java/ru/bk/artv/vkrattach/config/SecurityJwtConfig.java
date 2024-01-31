package ru.bk.artv.vkrattach.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import ru.bk.artv.vkrattach.config.security.*;
import ru.bk.artv.vkrattach.config.security.serializers.*;
import ru.bk.artv.vkrattach.dao.repository.DeactivatedTokenRepository;

import java.text.ParseException;
import java.util.Arrays;

/**
 * Все бины для JWT аутентификации
 */
@Configuration
//@PropertySource("classpath:/application-keys.yml")
public class SecurityJwtConfig {
    //Сериалайзеры /десериалайзеры токенов
    @Bean
    public AccessTokenJwsStringSerializer accessTokenJwsStringSerializer
    (@Value("${jwt.access-token-key}") String accessTokenKey) throws ParseException, KeyLengthException {
        return new AccessTokenJwsStringSerializerImpl(
                new MACSigner(OctetSequenceKey.parse(accessTokenKey)));
    }

    @Bean
    public AccessTokenJwsStringDeserializer accessTokenJwsStringDeserializer
            (@Value("${jwt.access-token-key}") String accessTokenKey) throws ParseException, JOSEException {
        return new AccessTokenJwsStringDeserializerImpl(
                new MACVerifier(OctetSequenceKey.parse(accessTokenKey)));
    }

    @Bean
    public RefreshTokenJweStringSerializer refreshTokenJweStringSerializer
            (@Value("${jwt.refresh-token-key}") String refreshTokenKey) throws ParseException, KeyLengthException {
        return new RefreshTokenJweStringSerializerImpl(
                new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey)));
    }

    @Bean
    public RefreshTokenJweStringDeserializer refreshTokenJweStringDeserializer
            (@Value("${jwt.refresh-token-key}") String refreshTokenKey) throws ParseException, KeyLengthException {
        return new RefreshTokenJweStringDeserializerImpl(
                new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey)));
    }

    //Фабрики токенов
    @Bean
    public AccessTokenFactory accessTokenFactory() {
        return new DefaultAccessTokenFactory();
    }

    @Bean
    public RefreshTokenFactory refreshTokenFactory() {
        return new DefaultRefreshTokenFactory();
    }

    //Фильтр логаута
    @Bean
    public JwtLogoutFilter jwtLogoutFilter(DeactivatedTokenRepository deactivatedTokenRepository) {
        return new JwtLogoutFilter(deactivatedTokenRepository);
    }

    //Refresh токен фильтр
    @Bean
    public RefreshTokenFilter refreshTokenFilter(AccessTokenJwsStringSerializer accessTokenJwsStringSerializer,
                                                 RefreshTokenJweStringSerializer refreshTokenJweStringSerializer) {
        return new RefreshTokenFilter(refreshTokenJweStringSerializer, accessTokenJwsStringSerializer);
    }

    @Bean
    BasicAuthenticationStrategy usernamePasswordAuthenticationStrategy
            (RefreshTokenJweStringSerializer refreshTokenJweStringSerializer) {
        return new BasicAuthenticationStrategy(refreshTokenJweStringSerializer);
    }

    //Конвертер токенов в аутентификацию
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter
            (RefreshTokenJweStringDeserializer refreshTokenJweStringDeserializer,
             AccessTokenJwsStringDeserializer accessTokenJwsStringDeserializer) {
        return new JwtAuthenticationConverter(accessTokenJwsStringDeserializer, refreshTokenJweStringDeserializer);
    }

    @Bean
    public TokenAuthenticationFilter jwtAuthenticationFilter(DeactivatedTokenRepository deactivatedTokenRepository,
                                                             JwtAuthenticationConverter jwtAuthenticationConverter) {
        var tokenAuthenticationUserDetailsService = new TokenAuthenticationUserDetailsService(deactivatedTokenRepository);
        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                tokenAuthenticationUserDetailsService);
        var providerManager = new ProviderManager(Arrays.asList(authenticationProvider));
        return new TokenAuthenticationFilter(providerManager, jwtAuthenticationConverter);

    }
}
