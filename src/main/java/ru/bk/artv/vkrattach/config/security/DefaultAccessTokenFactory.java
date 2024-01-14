package ru.bk.artv.vkrattach.config.security;

import java.time.Duration;
import java.time.Instant;

/**
 * превращает refresh токен в Access токен. Убирает лишние права пользователя и преобразовывает GRANT права в обычные.
 */
public class DefaultAccessTokenFactory implements AccessTokenFactory {

    private Duration tokenTtl = Duration.ofMinutes(5);

    @Override
    public Token createToken(Token token) {
        var now = Instant.now();
        return new Token(token.id(), token.subject(),
                token.authorities().stream()
                        .filter(authority -> authority.startsWith("GRANT_"))
                        .map(authority -> authority.replace("GRANT_", ""))
                        .toList(), now, now.plus(this.tokenTtl), Token.TokenType.ACCESS);
    }

    public void setTokenTtl(Duration tokenTtl) {
        this.tokenTtl = tokenTtl;
    }
}
