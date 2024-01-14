package ru.bk.artv.vkrattach.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.ModeratorUser;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.web.dto.LecturerDto;
import ru.bk.artv.vkrattach.web.dto.LecturerFullDto;
import ru.bk.artv.vkrattach.services.LecturerService;
import ru.bk.artv.vkrattach.services.TokenUserToDefaultUserConverter;

import java.util.List;

/**
 * REST-сервис, предоставляющий CRUD для работы с Руководителями ВКР (Lecturer), а также получение этих руководителей
 * в списках по запросам пользователей. Get одного руководителя нет, ибо не возникало необходимости.
 */
@Slf4j
@RestController
@RequestMapping(path = "rest/lecturer")
public class LecturerRestController {

    //Сервис для работы с Lecturer
    private final LecturerService lecturerService;

    //Конвертер JWT аутентификации в расширенные данные пользователя
    private final TokenUserToDefaultUserConverter converter;

    public LecturerRestController(LecturerService lecturerService, TokenUserToDefaultUserConverter converter) {
        this.lecturerService = lecturerService;
        this.converter = converter;
    }

    /**
     * Предоставляет список руководителей ВКР в соответствии с параметрами запроса, которые могут быть, а могут не быть.
     *
     * @param department кафедра
     * @param faculty специальность
     * @param year год набора
     * @param tokenUser аутентифицироанный пользователь
     * @return список Руководителей ВКР
     */

    @GetMapping(path = "/getlecturers")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public List<LecturerDto> getLecturers(@RequestParam(value = "department", required = false) String department,
                                          @RequestParam(value = "faculty", required = false) String faculty,
                                          @RequestParam(value = "year", required = false) String year,
                                          @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);

        return switch (user.getRole()) {
            case USER -> lecturerService.getSimpleLecturers(department);
            case MODERATOR -> lecturerService.getFullLecturers(((ModeratorUser) user).getDepartment(), faculty, year);
            case ADMIN -> lecturerService.getFullLecturers(department, faculty, year);
        };
    }

    /**
     * Изменение данных руководителя ВКР
     *
     * @param lecturer изменяемый Lecturer
     * @return измененного Lecturer
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public LecturerFullDto patchLecturer(@RequestBody @Valid LecturerFullDto lecturer) {
        return lecturerService.patchLecturer(lecturer);
    }

    /**
     * Сохранение данных руководителя ВКР
     *
     * @param lecturer сохраняемый Lecturer
     * @return сохраненный Lecturer
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public LecturerFullDto putLecturer(@RequestBody @Valid LecturerFullDto lecturer) {
        return lecturerService.putLecturer(lecturer);
    }

    /**
     * Удаление из базы руководителя ВКР
     *
     * @param id удаляемый Lecturer
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void deleteLecturer(@RequestParam("id") Long id) {
        lecturerService.deleteLecturer(id);
    }
}
