package ru.bk.artv.vkrattach.config.security.serializers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.bk.artv.vkrattach.config.security.Token;

import java.text.ParseException;
import java.util.UUID;

public class RefreshTokenJweStringDeserializerImpl implements RefreshTokenJweStringDeserializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenJweStringDeserializerImpl.class);

    private final JWEDecrypter jweDecrypter;

    public RefreshTokenJweStringDeserializerImpl(JWEDecrypter jweDecrypter) {
        this.jweDecrypter = jweDecrypter;
    }

    @Override
    public Token deserialize(String string) {
        try {
            var encryptedJWT = EncryptedJWT.parse(string);
            encryptedJWT.decrypt(this.jweDecrypter);
            var claimsSet = encryptedJWT.getJWTClaimsSet();
            return new Token(UUID.fromString(claimsSet.getJWTID()), claimsSet.getSubject(),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant(), Token.TokenType.REFRESH);
        } catch (ParseException | JOSEException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }

        return null;
    }
}
