package ru.bk.artv.vkrattach.web;

import com.turkraft.springfilter.boot.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.business.UserRegistrationService;
import ru.bk.artv.vkrattach.domain.FacultiesMap;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.YearsOfRecruitmentMap;
import ru.bk.artv.vkrattach.domain.dto.UserToClientDTO;
import ru.bk.artv.vkrattach.domain.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping(path = "/rest/user", produces = "application/json")
public class UserRestController {

    UserRegistrationService userRegistrationService;
    YearsOfRecruitmentMap yearOfRecruitment;
    FacultiesMap faculties;

    public UserRestController(UserRegistrationService userRegistrationService, YearsOfRecruitmentMap yearOfRecruitment, FacultiesMap faculties) {
        this.userRegistrationService = userRegistrationService;
        this.yearOfRecruitment = yearOfRecruitment;
        this.faculties = faculties;
    }

    @GetMapping
    public UserToClientDTO getUser(@AuthenticationPrincipal DefaultUser user) {
        return userRegistrationService.getUser(user);
    }

    @GetMapping (path = "/getusers")
    public Map<Long, UserToClientDTO> getUsers(@Filter Specification<SimpleUser> specs) {
        return userRegistrationService.findUsers(specs);
    }

    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewUser(@Valid @RequestBody UserToPatchDTO userDTO, @AuthenticationPrincipal DefaultUser user) {
        log.info("AuthorizationRestController.registerUser() : " + userDTO);
        if (user == null && userDTO.getRole() == Role.USER || user.getRole() == Role.ADMIN){
            userRegistrationService.registerNewUser(userDTO);
        } else {
            throw new ResourceNotSavedException("Для регистрации пользователя необходимо выйти из аккаунта");
        }
    }

    @PatchMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void patchUser(@RequestBody UserToPatchDTO userDTO, @AuthenticationPrincipal DefaultUser user) {
        log.info("AuthorizationRestController.patchUser() : " + userDTO);
        userRegistrationService.patchUser(userDTO, user);
    }
}
