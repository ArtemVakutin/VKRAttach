package ru.bk.artv.vkrattach.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.config.security.auth.TokenUser;
import ru.bk.artv.vkrattach.dao.repository.DefaultUserRepository;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;

import java.util.function.Function;

@AllArgsConstructor
@Service
public class TokenUserToDefaultUserConverter implements Function<TokenUser, DefaultUser> {

    DefaultUserRepository defaultUserRepository;

    @Override
    public DefaultUser apply(TokenUser tokenUser) {
        return defaultUserRepository.findByLoginIgnoreCase(tokenUser.getUsername());
    }
}
