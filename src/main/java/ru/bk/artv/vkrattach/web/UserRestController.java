package ru.bk.artv.vkrattach.web;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.web.dto.UserDto;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.services.TokenUserToDefaultUserConverterImpl;
import ru.bk.artv.vkrattach.services.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * REST-контроллер для управления пользоватлеями
 */
@Slf4j
@RestController
@RequestMapping(path = "/rest/user", produces = "application/json")
public class UserRestController {

    //сервис для управления пользоватлеями
    private final UserService userService;
    //валидатор
    private final Validator validator;
    //конвертер аутентификации
    private final TokenUserToDefaultUserConverterImpl converter;

    public UserRestController(UserService userService, Validator validator, TokenUserToDefaultUserConverterImpl converter) {
        this.userService = userService;
        this.validator = validator;
        this.converter = converter;
    }

    /**
     * Возвращает данные о текущем аутентифицированном пользователе
     *
     * @param tokenUser аутентифицированный JWT пользователь
     * @return аутентифицированного пользователя или ошибку
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public UserDto getUser(@AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);
        return userService.getUser(user);
    }

    /**
     * Получение пользователя по id
     *
     * @param id айдишник запрашиваемого пользователя
     * @param tokenUser аутентифицированный пользователь
     * @return пользователя в Dto
     */
    @GetMapping(params = "id")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public UserDto getUser(@RequestParam Long id, @AuthenticationPrincipal TokenUser tokenUser) {
        return userService.getUser(id);
    }

    /**
     * Запрос списков пользователей по роли и иным фильтрам
     *
     * @param role роль запрашиваемого пользователя
     * @param filter иные фильтры
     * @return список пользователей или ошибку при их отсутствии
     */
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @GetMapping (path = "/getusers")
    @PreAuthorize("isAuthenticated()")
    public List<UserDto> getUsers(@RequestParam(name = "role", defaultValue = "USER") Role role,
                                  @Filter Specification<SimpleUser> filter) {
        if(role == Role.USER) {
            return userService.findUsers(filter);
        }
        return userService.findUsers(role);
    }

    /**
     * Регистрация нового пользователя
     *
     * @param userDTO добавляемый пользователь
     * @param tokenUser аутентифицированный пользователь
     * @return добавленный пользователь
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') or isAnonymous()")
    public UserDto addUser(@RequestBody @Valid UserDto userDTO, @AuthenticationPrincipal TokenUser tokenUser) {
            if (tokenUser == null && !userDTO.getRole().equals(Role.USER)) {
                throw new AccessDeniedException("Попытка зарегистрировать пользователя с правами, отличными от User");
            }
            validateNewUser(userDTO);
            return userService.registerNewUser(userDTO);
    }

    //валидация добавляемого пользователя
    private void validateNewUser(UserDto userDTO) {
        Set<ConstraintViolation<UserDto>> violations;
        if (userDTO.getRole()==Role.USER) {
            violations = validator.validate(userDTO, UserDto.ValidationForRegisterUser.class, UserDto.ValidationPassword.class);
        } else if (userDTO.getRole()==Role.MODERATOR) {
            violations = validator.validate(userDTO, UserDto.ValidationForRegisterModerator.class, UserDto.ValidationPassword.class);
        } else {
            violations = validator.validate(userDTO, UserDto.ValidationForRegisterAdmin.class, UserDto.ValidationPassword.class);
        }
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("UserDTO violation exception", violations);
        }
    }


    /**
     * Модификация пользователей
     *
     * @param userDTO изменяемый пользователь
     * @param tokenUser аутентифицированный пользователь
     * @return измененного пользователя
     */
    @PatchMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public UserDto patchUser(@RequestBody UserDto userDTO, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);
        validatePatchUser(userDTO);
        return switch (user.getRole()) {
            case ADMIN -> userService.patchUser(userDTO);
            case MODERATOR -> null;
            case USER -> userService.patchUser(userDTO, user);
        };
    }

    //валидация модифицируемого пользователя. Внимание vallidator.validate возвращает immutable collection!
    private void validatePatchUser(UserDto userDTO) {
        Set<ConstraintViolation<UserDto>> violations = new HashSet<>();

        if (userDTO.getRole()==Role.USER) {
            violations.addAll(validator.validate(userDTO, UserDto.ValidationForRegisterUser.class, UserDto.ValidationId.class));
        } else if (userDTO.getRole()==Role.MODERATOR) {
            violations.addAll(validator.validate(userDTO, UserDto.ValidationForRegisterModerator.class, UserDto.ValidationId.class));
        } else {
            violations.addAll(validator.validate(userDTO, UserDto.ValidationForRegisterAdmin.class, UserDto.ValidationId.class));
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            violations.addAll(validator.validate(userDTO, UserDto.ValidationPassword.class));
        }

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("UserDTO violation exception", violations);
        }
    }

    /**
     * Удаляет пользователя
     *
     * @param userId удаляемый пользователь
     * @param tokenUser аутентифицированный пользователь
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public void deleteUser(@RequestParam(name = "id") Long userId, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);

        if (!user.getId().equals(userId)) {
            userService.deleteUser(userId);
        } else {
            throw new AccessDeniedException("You are not admin to delete users or you want to delete yourself, baby");
        }
    }

    /**
     * Изменение пароля пользователя
     *
     * @param userDto с полями oldPassword и password, валидируется password
     * @param tokenUser пользователь из аутентификации
     */
    @PatchMapping(path = "/password")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
        public void changeUserPassword(@RequestBody @Validated({UserDto.ValidationPassword.class}) UserDto userDto, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);

        userService.changeUserPassword(userDto, user);
    }
}
