package ru.bk.artv.vkrattach.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.services.LecturerService;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.dto.LecturerDTO;

import javax.validation.Valid;
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
    public List<Lecturer> getLecturers(@RequestParam("department") String department) {
        return lecturerService.getLecturers(department);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public LecturerDTO patchLecturer(@RequestBody @Valid LecturerDTO lecturer) {
        return lecturerService.patchLecturer(lecturer);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public LecturerDTO putLecturer(@RequestBody @Valid LecturerDTO lecturer) {
        return lecturerService.putLecturer(lecturer);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteLecturer(@RequestParam("id") Long id,
                               @RequestParam(name = "recover", required = false, defaultValue = "false") Boolean recover) {
            lecturerService.deleteLecturer(id);
            }
}
