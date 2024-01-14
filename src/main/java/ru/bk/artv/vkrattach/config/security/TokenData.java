package ru.bk.artv.vkrattach.config.security;


/**
 * Access токен
 *
 * @param accessToken сам токен
 * @param accessTokenExpiry срок действмия токена
 */
public record TokenData(String accessToken, String accessTokenExpiry) {
}
