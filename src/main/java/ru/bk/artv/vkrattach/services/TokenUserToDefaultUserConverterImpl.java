package ru.bk.artv.vkrattach.services;

import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.dao.repository.DefaultUserRepository;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;

/**
 * Для перевода TokenUser (берется из токена JWT аутентификации) в DefaultUser. Имплементация.
 * Запрашивает DefaultUser из базы данных
 */
@Service
public class TokenUserToDefaultUserConverterImpl implements TokenUserToDefaultUserConverter {

    DefaultUserRepository defaultUserRepository;

    public TokenUserToDefaultUserConverterImpl(DefaultUserRepository defaultUserRepository) {
        this.defaultUserRepository = defaultUserRepository;
    }

    /**
     * Запрашивает пользователя для Rest-контроллеров из базы данных по аутентифицированному пользователю
     *
     * @param tokenUser токен, полученный из JWT-аутентификации
     * @return DefaultUser или null в случае, если tokenUser null
     */
    @Override
    public DefaultUser convertToDefaultUser(TokenUser tokenUser) {
        if (tokenUser != null) {
            return defaultUserRepository.findByLoginIgnoreCase(tokenUser.getUsername());
        }
        return null;
    }
}
