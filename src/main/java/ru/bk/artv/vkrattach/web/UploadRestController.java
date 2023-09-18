package ru.bk.artv.vkrattach.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.dto.LecturerDTO;
import ru.bk.artv.vkrattach.dto.ThemeDTO;
import ru.bk.artv.vkrattach.dto.UploadAnswer;
import ru.bk.artv.vkrattach.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;
import ru.bk.artv.vkrattach.services.UploadService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(path = "rest/data/input")
public class UploadRestController {
    UploadService uploadService;

    public UploadRestController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(path = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UploadAnswer<UserToPatchDTO> checkUploadUsers(HttpSession session, @RequestParam(name = "data") MultipartFile file) {
        if (file.isEmpty()) {
            throw new UploadResourceException("File is empty" + file.toString());
        }
        log.info("FileName : {}, size : {}", file.getOriginalFilename(), file.getSize());

        UploadAnswer<UserToPatchDTO> uploadAnswer = uploadService.checkUploadUsers(file, session);
        log.info("answer id : {} and data : {}", uploadAnswer.getDataId(), uploadAnswer.getObjects().stream().map(UserToPatchDTO::toString).collect(Collectors.joining(" ")));
        return uploadAnswer;
    }

    @GetMapping(path = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUploadedUsers(HttpSession session, @RequestParam(name = "dataId") String id) {
        List<UserToPatchDTO> users = (List<UserToPatchDTO>)session.getAttribute(id);
        log.info("Try to register users with session object id : {}", id);
        uploadService.registerUploadUsers(users);
        log.info("All users are registered");
        session.removeAttribute(id);
    }

    @PostMapping(path = "/themes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UploadAnswer<ThemeDTO> checkUploadThemes(HttpSession session,
                                                    @RequestParam(name = "data") MultipartFile file,
                                                    @RequestParam(name = "department") String department,
                                                    @RequestParam(name = "faculty") String faculty,
                                                    @RequestParam(name = "yearOfRecruitment") String year) {
        if (file.isEmpty()) {
            throw new UploadResourceException("File is empty" + file.toString());
        }
        log.info("Department: {}, yearOfRecruitment: {}, faculty: {}, FileName : {}, size : {}",
                department, year, faculty, file.getOriginalFilename(), file.getSize());

        UploadAnswer<ThemeDTO> uploadAnswer = uploadService.checkUploadThemes(file, session, department, faculty, year);

        log.info("answer id : {} and data : {}", uploadAnswer.getDataId(), uploadAnswer.getObjects().stream()
                .map(ThemeDTO::toString).collect(Collectors.joining(" ")));
        return uploadAnswer;
    }

    @GetMapping(path = "/themes")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUploadedThemes(HttpSession session, @RequestParam(name = "dataId") String id) {
        List<ThemeDTO> themes = (List<ThemeDTO>)session.getAttribute(id);
        log.info("Try to register users with session object id : {}", id);
        uploadService.registerUploadThemes(themes);
        log.info("All users are registered");
        session.removeAttribute(id);
    }

    @PostMapping(path = "/lecturers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UploadAnswer<LecturerDTO> checkUploadThemes(HttpSession session,
                                                       @RequestParam(name = "data") MultipartFile file,
                                                       @RequestParam(name = "department") String department
                                                       ) {
        if (file.isEmpty()) {
            throw new UploadResourceException("File is empty" + file.toString());
        }
        log.info("Department: {}, FileName : {}, size : {}",
                department, file.getOriginalFilename(), file.getSize());

        UploadAnswer<LecturerDTO> uploadAnswer = uploadService.checkUploadLecturers(file, session, department);

        log.info("answer id : {} and data : {}", uploadAnswer.getDataId(), uploadAnswer.getObjects().stream()
                .map(LecturerDTO::toString).collect(Collectors.joining(" ")));
        return uploadAnswer;
    }

    @GetMapping(path = "/lecturers")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUploadedLecturers(HttpSession session, @RequestParam(name = "dataId") String id) {
        List<LecturerDTO> themes = (List<LecturerDTO>)session.getAttribute(id);
        log.info("Try to register users with session object id : {}", id);
        uploadService.registerUploadLecturers(themes);
        log.info("All users are registered");
        session.removeAttribute(id);
    }

}
