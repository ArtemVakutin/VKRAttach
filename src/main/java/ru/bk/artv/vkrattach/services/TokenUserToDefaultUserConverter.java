package ru.bk.artv.vkrattach.services;

import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;


/**
 * Для перевода TokenUser (берется из JWT токена аутентификации) в DefaultUser
 */

public interface TokenUserToDefaultUserConverter {

    public DefaultUser convertToDefaultUser(TokenUser tokenUser);

}
