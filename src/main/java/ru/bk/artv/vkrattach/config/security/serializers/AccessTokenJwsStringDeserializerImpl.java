package ru.bk.artv.vkrattach.config.security.serializers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import ru.bk.artv.vkrattach.config.security.Token;

import java.text.ParseException;
import java.util.UUID;

@Slf4j
public class AccessTokenJwsStringDeserializerImpl implements AccessTokenJwsStringDeserializer {

    private final JWSVerifier jwsVerifier;

    public AccessTokenJwsStringDeserializerImpl(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    public Token deserialize(String string) {
        try {
            var signedJWT = SignedJWT.parse(string);
            if (signedJWT.verify(this.jwsVerifier)) {
                var claimsSet = signedJWT.getJWTClaimsSet();
                return new Token(UUID.fromString(claimsSet.getJWTID()), claimsSet.getSubject(),
                        claimsSet.getStringListClaim("authorities"),
                        claimsSet.getIssueTime().toInstant(),
                        claimsSet.getExpirationTime().toInstant(), Token.TokenType.ACCESS);
            }
        } catch (ParseException | JOSEException exception) {
            log.error(exception.getMessage(), exception);
        }
        return null;
    }
}
