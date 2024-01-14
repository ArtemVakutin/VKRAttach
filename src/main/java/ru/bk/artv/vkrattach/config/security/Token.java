package ru.bk.artv.vkrattach.config.security;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Сам токен. Без разницы какой. Token type нужен, чтобы при авторизации по Access токену не лазила в базу
 * заблокированных Refresh-токенов
 */

public record Token(UUID id, String subject, List<String> authorities, Instant createdAt,
                    Instant expiresAt, TokenType tokenType) {

    public enum TokenType {
        ACCESS, REFRESH
    }
}


