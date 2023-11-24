package ru.bk.artv.vkrattach.web;

import com.turkraft.springfilter.boot.Filter;
import jakarta.persistence.Converter;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.config.security.auth.TokenUser;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.Role;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.dto.UserDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.services.TokenUserToDefaultUserConverter;
import ru.bk.artv.vkrattach.services.UserService;
import ru.bk.artv.vkrattach.services.mappers.UserMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping(path = "/rest/user", produces = "application/json")
@AllArgsConstructor
public class UserRestController {

    UserMapper userMapper;
    Function<TokenUser, DefaultUser> converter;
    UserService userService;
    Validator validator;

    @GetMapping
    public UserDTO getUser(@AuthenticationPrincipal TokenUser user) {
        DefaultUser defaultUser = converter.apply(user);
        return userMapper.toUserDTO(defaultUser);
    }

    @GetMapping(params = "id")
    public UserDTO getUser(@RequestParam Long id, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.apply(tokenUser);
        if (user.getRole()== Role.ADMIN ||user.getRole()== Role.MODERATOR) {
            return userService.getUser(id);
        } else {
            throw new AccessDeniedException("Access to id : " + id + " denied");
        }
    }

    @GetMapping (path = "/getusers")
    public List<UserDTO> getUsers(@RequestParam(name = "role", defaultValue = "USER") Role role,
                                  @Filter Specification<SimpleUser> filter) {
        if(role == Role.USER) {
            return userService.findUsers(filter);
        }
        return userService.findUsers(role);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({UserDTO.ValidationForRegisterUser.class})
    public UserDTO addUser(@RequestBody @Valid UserDTO userDTO, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.apply(tokenUser);
        validateNewUser(userDTO);

        if (user == null && userDTO.getRole() == Role.USER || user != null && user.getRole() == Role.ADMIN){
            return userService.registerNewUser(userDTO);
        } else {
            throw new ResourceNotSavedException("Для регистрации пользователя необходимо выйти из аккаунта");
        }
    }

    private void validateNewUser(UserDTO userDTO) {
        Set<ConstraintViolation<UserDTO>> violations;
        if (userDTO.getRole()==Role.USER) {
            violations = validator.validate(userDTO, UserDTO.ValidationForRegisterUser.class, UserDTO.ValidationPassword.class);
        } else if (userDTO.getRole()==Role.MODERATOR) {
            violations = validator.validate(userDTO, UserDTO.ValidationForRegisterModerator.class, UserDTO.ValidationPassword.class);
        } else {
            violations = validator.validate(userDTO, UserDTO.ValidationForRegisterAdmin.class, UserDTO.ValidationPassword.class);
        }
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("UserDTO violation exception", violations);
        }
    }

    @PatchMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO patchUser(@RequestBody UserDTO userDTO, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.apply(tokenUser);
        validatePatchUser(userDTO);
        if (user.getRole().equals(Role.ADMIN)) {
            return userService.patchUser(userDTO);
        } else {
            return userService.patchUser(userDTO, user);
        }
    }

    private void validatePatchUser(UserDTO userDTO) {
        Set<ConstraintViolation<UserDTO>> violations;

        if (userDTO.getRole()==Role.USER) {
            violations = validator.validate(userDTO, UserDTO.ValidationForRegisterUser.class, UserDTO.ValidationId.class);
        } else if (userDTO.getRole()==Role.MODERATOR) {
            violations = validator.validate(userDTO, UserDTO.ValidationForRegisterModerator.class, UserDTO.ValidationId.class);
        } else {
            violations = validator.validate(userDTO, UserDTO.ValidationForRegisterAdmin.class, UserDTO.ValidationId.class);
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            violations.addAll(validator.validate(userDTO, UserDTO.ValidationPassword.class));
        }

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("UserDTO violation exception", violations);
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestParam(name = "id") Long userId, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.apply(tokenUser);
        if (user.getRole() == Role.ADMIN && user.getId() != userId) {
            userService.deleteUser(userId);
        } else {
            throw new AccessDeniedException("You are not admin to delete users or you want to delete yourself, baby");
        }
    }
}
