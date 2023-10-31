package ru.bk.artv.vkrattach.web;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.services.UserService;
import ru.bk.artv.vkrattach.domain.user.Role;
import ru.bk.artv.vkrattach.dto.UserToClientDTO;
import ru.bk.artv.vkrattach.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;

import java.util.List;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping(path = "/rest/user", produces = "application/json")
@Validated
public class UserRestController {

    UserService userService;


    public UserRestController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping
    public UserToClientDTO getUser(@AuthenticationPrincipal DefaultUser user) {
        return userService.getUser(user);
    }

    @GetMapping(params = "id")
    public UserToClientDTO getUser(@RequestParam Long id,@AuthenticationPrincipal DefaultUser user) {
        if (user.getRole()== Role.ADMIN ||user.getRole()== Role.MODERATOR) {
            return userService.getUser(id);
        } else {
            throw new AccessDeniedException("Access to id : " + id + " denied");
        }

    }

    @GetMapping (path = "/getusers")
    public List<UserToClientDTO> getUsers(@RequestParam(name = "role", defaultValue = "USER") Role role,
                                          @Filter Specification<SimpleUser> filter) {
        if(role == Role.USER) {
            return userService.findUsers(filter);
        }
        return userService.findUsers(role);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({UserToPatchDTO.ValidationStepForRegister.class})
    public Long addUser(@RequestBody @Valid UserToPatchDTO userDTO, @AuthenticationPrincipal DefaultUser user) {
        log.info("AuthorizationRestController.registerUser() : " + userDTO);
        if (user == null && userDTO.getRole() == Role.USER || user != null && user.getRole() == Role.ADMIN){
            return userService.registerNewUser(userDTO);
        } else {
            throw new ResourceNotSavedException("Для регистрации пользователя необходимо выйти из аккаунта");
        }
    }

    @PatchMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void patchUser(@RequestBody UserToPatchDTO userDTO, @AuthenticationPrincipal DefaultUser user) {
        log.info("AuthorizationRestController.patchUser() : userDTO : " + userDTO + "||| User : " + user.getId()
        + " with Login " + user.getLogin()+ " with Role " + user.getRole());
        if (userDTO.getPassword() != "" && userDTO.getPassword() != null) {
            userService.patchSimpleUserWithPassword(userDTO, user);
        } else {
            userService.patchSimpleUser(userDTO, user);
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@RequestParam(name = "id") Long userId, @AuthenticationPrincipal DefaultUser user) {
        log.info("AuthorizationRestController.deleteUser() : " + userId + " by " + user.getLogin());
        if (user.getRole() == Role.ADMIN && user.getId() != userId) {
            userService.deleteUser(userId);
        } else {
            throw new AccessDeniedException("You are not admin to delete users or you want to delete yourself, baby");
        }
    }
}
