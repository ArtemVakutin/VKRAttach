package ru.bk.artv.vkrattach.config.security;

/**
 * Интерфейс для фабрики access токенов
 */
public interface AccessTokenFactory {
    Token createToken(Token token);
}
