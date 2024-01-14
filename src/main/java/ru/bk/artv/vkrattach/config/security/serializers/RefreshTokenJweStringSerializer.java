package ru.bk.artv.vkrattach.config.security.serializers;

import ru.bk.artv.vkrattach.config.security.Token;

public interface RefreshTokenJweStringSerializer {
    String serialize(Token token);
}
