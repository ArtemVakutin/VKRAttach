package ru.bk.artv.vkrattach.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.business.UserRegistrationService;
import ru.bk.artv.vkrattach.domain.FacultiesList;
import ru.bk.artv.vkrattach.domain.User;
import ru.bk.artv.vkrattach.domain.YearOfRecruitmentList;
import ru.bk.artv.vkrattach.domain.dto.UserDTO;
import ru.bk.artv.vkrattach.domain.dto.UserRegistrationDTO;

import java.util.Map;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping(path = "/rest/registration", produces = "application/json")
public class AuthorizationRestController {

    UserRegistrationService userRegistrationService;
    YearOfRecruitmentList yearOfRecruitment;
    FacultiesList faculties;

    public AuthorizationRestController(UserRegistrationService userRegistrationService, YearOfRecruitmentList yearOfRecruitment, FacultiesList faculties) {
        this.userRegistrationService = userRegistrationService;
        this.yearOfRecruitment = yearOfRecruitment;
        this.faculties = faculties;
    }

    @GetMapping("/getuser")
    public UserDTO getUser(@AuthenticationPrincipal User user) {
        return userRegistrationService.getUser(user);
    }

    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserRegistrationDTO userDTO) {
        log.info("AuthorizationRestController.registerUser() : " + userDTO);
        userRegistrationService.registerNewUser(userDTO);
    }

    @PatchMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void patchUser(@RequestBody UserRegistrationDTO userDTO, @AuthenticationPrincipal User user) {
        log.info("AuthorizationRestController.patchUser() : " + userDTO);
        userRegistrationService.patchUser(userDTO, user);
    }


    @GetMapping(path = "/getyears", produces = "application/json")
    public Map<String, String> getYearsOfRecruitmentWeb() {
        return yearOfRecruitment.getYears();
    }

    @GetMapping(path = "/getfaculties", produces = "application/json")
    public Map<String, String> getFacultiesWeb() {
        return faculties.getFaculties();
    }

}
