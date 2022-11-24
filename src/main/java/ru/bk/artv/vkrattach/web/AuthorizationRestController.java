package ru.bk.artv.vkrattach.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.business.UserRegistrationService;
import ru.bk.artv.vkrattach.domain.*;
import ru.bk.artv.vkrattach.domain.dto.UserAuthorizationStatus;
import ru.bk.artv.vkrattach.domain.dto.UserDTO;
import ru.bk.artv.vkrattach.domain.dto.UserRegistrationDTO;

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
    public UserDTO getUser(@AuthenticationPrincipal User user){
        return userRegistrationService.getUser(user);
    }

    @GetMapping("/isauthorized")
    public UserAuthorizationStatus isAuthorised(@AuthenticationPrincipal User user) {
        if (user == null) {
            return new UserAuthorizationStatus(false);
        }
        return new UserAuthorizationStatus(true);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserRegistrationDTO user){
        log.info("AuthorizationRestController.registerUser())" + user);
        userRegistrationService.registerNewUser(user);
    }

    @GetMapping(path = "/getyears", produces = "application/json")
    public YearOfRecruitmentList getYearsOfRecruitmentWeb(){
        return yearOfRecruitment;
    }

    @GetMapping(path = "/getfaculties", produces = "application/json")
    public FacultiesList getFacultiesWeb(){
        return faculties;
    }

//    @PostMapping(path = "/order", consumes = "application/json")

}
