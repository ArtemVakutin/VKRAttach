package ru.bk.artv.vkrattach.config.security.serializers;

import ru.bk.artv.vkrattach.config.security.Token;

public interface AccessTokenJwsStringDeserializer {
    Token deserialize(String string);
}
