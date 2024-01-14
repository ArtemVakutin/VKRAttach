package ru.bk.artv.vkrattach.services.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Представляет объект данных "Пользователь".
 * <p>
 * Абстрактный. Не наследуется от UserDetails и не используется
 * при аутентификации, так как аутентификация по JWT без запросов в базу данных.
 * <p>
 * Данные хранятся в единой таблице, дискриминатор по user_role. Логин может не совпадать с емейлом.
 */
@Entity
@Table(name = "vkr_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_role")
@Data
public abstract class DefaultUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    @Column(name = "user_login")
    private String login;

    @NotBlank
    @Column(name = "user_password")
    private String password;

    @Column(name = "user_role", updatable = false, insertable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

}
