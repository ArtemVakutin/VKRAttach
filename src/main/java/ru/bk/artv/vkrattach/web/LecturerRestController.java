package ru.bk.artv.vkrattach.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.business.DomainService;
import ru.bk.artv.vkrattach.business.LecturerService;
import ru.bk.artv.vkrattach.domain.Lecturer;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "rest/lecturer")
public class LecturerRestController {

    LecturerService lecturerService;

    public LecturerRestController(DomainService domainService, LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping(path = "/getlecturers")
    @ResponseStatus(HttpStatus.OK)
    public Map<Long, Lecturer> getLecturers(@RequestParam("department") String department) {
        return lecturerService.getLecturers(department);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void patchLecturer(@RequestBody @Valid Lecturer lecturer) {
        lecturerService.patchLecturer(lecturer);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void putLecturer(@RequestBody @Valid Lecturer lecturer) {
        lecturerService.patchLecturer(lecturer);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteLecturer(@RequestParam("id") Long id,
                               @RequestParam(name = "recover", required = false, defaultValue = "false") Boolean recover) {
        if (recover){
            lecturerService.recoverLecturer(id);
        } else {
            lecturerService.deleteLecturer(id);
        }

    }
}
