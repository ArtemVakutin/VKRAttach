package ru.bk.artv.vkrattach.authentication;

/**
 * Используется в качестве полезной нагрузки при передаче клиенту
 * @param accessToken
 * @param accessTokenExpiry
 */
public record TokensDto(String accessToken, String accessTokenExpiry) {
}
