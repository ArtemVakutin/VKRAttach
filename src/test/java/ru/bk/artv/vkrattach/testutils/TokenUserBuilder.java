package ru.bk.artv.vkrattach.testutils;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.bk.artv.vkrattach.config.security.Token;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.services.model.user.AdminUser;
import ru.bk.artv.vkrattach.services.model.user.ModeratorUser;
import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class TokenUserBuilder {

    private String username = "User1";
    private String password = "nopassword";
    private Collection<? extends GrantedAuthority> authorities;
    private Token token = null;

    private TokenUserBuilder() {
    }

    public static TokenUserBuilder create() {
        return new TokenUserBuilder();
    }

    public TokenUserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public TokenUserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public TokenUser buildTokenUserRoleUser() {
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        TokenUser user = new TokenUser(username, password, authorities, token);
        return user;
    }
    public TokenUser buildTokenUserRoleModerator() {
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_MODERATOR"));
        TokenUser user = new TokenUser(username, password, authorities, token);
        return user;
    }
    public TokenUser buildTokenUserRoleAdmin() {
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        TokenUser user = new TokenUser(username, password, authorities, token);
        return user;
    }


}