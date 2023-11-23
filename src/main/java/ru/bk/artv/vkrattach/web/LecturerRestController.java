package ru.bk.artv.vkrattach.web;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.ModeratorUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.dto.LecturerDto;
import ru.bk.artv.vkrattach.dto.LecturerFullDto;
import ru.bk.artv.vkrattach.services.LecturerService;
import ru.bk.artv.vkrattach.domain.Lecturer;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "rest/lecturer")
public class LecturerRestController {

    LecturerService lecturerService;

    public LecturerRestController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping(path = "/getlecturers")
    @ResponseStatus(HttpStatus.OK)
    public List<LecturerDto> getLecturers(@RequestParam(value = "department", required = false) String department,
                                          @RequestParam(value = "faculty", required = false) String faculty,
                                          @RequestParam(value = "year", required = false) String year,
                                          @AuthenticationPrincipal DefaultUser user) {

        if (user instanceof ModeratorUser) {
            department = ((ModeratorUser) user).getDepartment();
        } else if (user instanceof SimpleUser) {
            return lecturerService.getSimpleLecturers(department);
        }
        return lecturerService.getFullLecturers(department, faculty, year);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public LecturerFullDto patchLecturer(@RequestBody @Valid LecturerFullDto lecturer) {
        return lecturerService.patchLecturer(lecturer);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public LecturerFullDto putLecturer(@RequestBody @Valid LecturerFullDto lecturer) {
        return lecturerService.putLecturer(lecturer);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteLecturer(@RequestParam("id") Long id,
                               @RequestParam(name = "recover", required = false, defaultValue = "false") Boolean recover) {
            lecturerService.deleteLecturer(id);
            }
}
