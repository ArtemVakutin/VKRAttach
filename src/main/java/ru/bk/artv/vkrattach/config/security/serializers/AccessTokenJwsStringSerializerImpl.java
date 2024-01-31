package ru.bk.artv.vkrattach.config.security.serializers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bk.artv.vkrattach.config.security.Token;

import java.util.Date;

@Slf4j
public class AccessTokenJwsStringSerializerImpl implements AccessTokenJwsStringSerializer {

    private final JWSSigner jwsSigner;

    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;

    public AccessTokenJwsStringSerializerImpl(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    public AccessTokenJwsStringSerializerImpl(JWSSigner jwsSigner, JWSAlgorithm jwsAlgorithm) {
        this.jwsSigner = jwsSigner;
        this.jwsAlgorithm = jwsAlgorithm;
    }

    @Override
    public String serialize(Token token) {
        var jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(token.id().toString())
                .build();
        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        var signedJWT = new SignedJWT(jwsHeader, claimsSet);
        try {
            signedJWT.sign(this.jwsSigner);

            return signedJWT.serialize();
        } catch (JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }

        return null;
    }

    public void setJwsAlgorithm(JWSAlgorithm jwsAlgorithm) {
        this.jwsAlgorithm = jwsAlgorithm;
    }

}
