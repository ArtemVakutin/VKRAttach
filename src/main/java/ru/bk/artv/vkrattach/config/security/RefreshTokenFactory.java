package ru.bk.artv.vkrattach.config.security;

import org.springframework.security.core.Authentication;

/**
 * Интерфейс для создания Refresh токенов
 */
public interface RefreshTokenFactory {
    Token createToken(Authentication authentication);
}
