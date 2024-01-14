package ru.bk.artv.vkrattach.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.web.dto.LecturerFullDto;
import ru.bk.artv.vkrattach.web.dto.ThemeDto;
import ru.bk.artv.vkrattach.web.dto.UploadAnswer;
import ru.bk.artv.vkrattach.web.dto.UserDto;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;
import ru.bk.artv.vkrattach.services.UploadDataService;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * Контроллер для загрузки данных на сервер посредством Docx или Excel файлов. Загрузка в два этапа. На первом
 * загружаемый файл парсятся и данные сохраняются во временном хранилище, а на втором соответственно идет подтверждение
 * сохранения пользователем и данные сохраняются.
 */
@Slf4j
@RestController
@RequestMapping(path = "rest/data/input")
public class UploadRestController {

    //сервис для загрузки пользователей
    UploadDataService<UserDto> uploadUsersService;

    //сервис для загрузки руководителей ВКР
    UploadDataService<LecturerFullDto> uploadLecturersService;

    //сервис для загрузки тем
    UploadDataService<ThemeDto> uploadThemesService;

    public UploadRestController(UploadDataService<UserDto> uploadUsersService, UploadDataService<LecturerFullDto> uploadLecturersService, UploadDataService<ThemeDto> uploadThemesService) {
        this.uploadUsersService = uploadUsersService;
        this.uploadLecturersService = uploadLecturersService;
        this.uploadThemesService = uploadThemesService;
    }

    /**
     * Парсинг excel файла в список загружаемых пользователей и их валидация.
     *
     * @param file Excel файл
     * @param paramsMap параметры пользователей (год набора, специальность (обязательно) и номер группы)
     * @return список загруженных, список тех, что с ошибками и айдишник для загрузки в случае наличия загружаемых
     */
    @PostMapping(path = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public UploadAnswer<UserDto> checkUploadUsers(@RequestParam(name = "data") MultipartFile file,
                                                  @RequestParam Map<String, String> paramsMap) {
        if (file.isEmpty()) {
            throw new UploadResourceException("File is empty" + file.toString());
        }
        log.info("FileName : {}, size : {}", file.getOriginalFilename(), file.getSize());

        UploadAnswer<UserDto> uploadAnswer = uploadUsersService.checkUploadData(file, paramsMap);

        log.info("answer id : {} and data : {}", uploadAnswer.getDataId(), uploadAnswer.getObjects().stream().map(UserDto::toString).collect(Collectors.joining(" ")));
        return uploadAnswer;
    }

    /**
     * Подтверждение загрузки пользователей
     *
     * @param id айдишник загрузки во временном хранилище
     */
    @GetMapping(path = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN"})
    public void registerUploadedUsers(@RequestParam(name = "dataId") String id) {
        log.info("Try to register users with session object id : {}", id);
        uploadUsersService.registerUploadData(id);
        log.info("All users are registered");
    }

    /**
     * Парсинг docx файла в список загружаемых тем и проверка на наличие тем по указанным данным
     *
     * @param file Docx файл (парсится по абзацам)
     * @param paramsMap кафедра, специальность, год набора
     * @return список загружаемых тем с айдишником или UploadResourceException в случае, если для конкретного года набора,
     * кафедры и специальности имеются какие либо темы ВКР.
     */
    @PostMapping(path = "/themes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public UploadAnswer<ThemeDto> checkUploadThemes(@RequestParam(name = "data") MultipartFile file,
                                                    @RequestParam Map<String, String> paramsMap) {
        if (file.isEmpty()) {
            throw new UploadResourceException("File is empty" + file.toString());
        }
        log.info("FileName : {}, size : {}",
                file.getOriginalFilename(), file.getSize());

        UploadAnswer<ThemeDto> uploadAnswer = uploadThemesService.checkUploadData(file, paramsMap);

        log.info("answer id : {} and data : {}", uploadAnswer.getDataId(), uploadAnswer.getObjects().stream()
                .map(ThemeDto::toString).collect(Collectors.joining(" ")));
        return uploadAnswer;
    }

    /**
     * Подтверждение загрузки тем и сохранение в базу
     *
     * @param id айдишник для загрузки
     */
    @GetMapping(path = "/themes")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN"})
    public void registerUploadedThemes(@RequestParam(name = "dataId") String id) {
        log.info("Try to register users with session object id : {}", id);
        uploadThemesService.registerUploadData(id);
        log.info("All users are registered");
    }

    /**
     * Парсинг excel файла в список загружаемых руководителей ВКР и их валидация
     *
     * @param file Excel файл
     * @param paramsMap кафедра
     * @return список загружаемых руководителей ВКР с айдишником, а также возможный список руководителей ВКР, которые
     * не могут быть загружены
     */
    @PostMapping(path = "/lecturers", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public UploadAnswer<LecturerFullDto> checkUploadLecturers(@RequestParam(name = "data") MultipartFile file,
                                                           @RequestParam Map<String, String> paramsMap) {
        if (file.isEmpty()) {
            throw new UploadResourceException("File is empty" + file.toString());
        }
        log.info("FileName : {}, size : {}",
                file.getOriginalFilename(), file.getSize());

        UploadAnswer<LecturerFullDto> uploadAnswer = uploadLecturersService.checkUploadData(file, paramsMap);

        log.info("answer id : {} and data : {}", uploadAnswer.getDataId(), uploadAnswer.getObjects().stream()
                .map(LecturerFullDto::toString).collect(Collectors.joining(" ")));
        return uploadAnswer;
    }

    /**
     * Подтверждение для загрузки и сохранение
     *
     * @param id айдишник загрузки
     */
    @GetMapping(path = "/lecturers")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN"})
    public void registerUploadedLecturers(@RequestParam(name = "dataId") String id) {
        log.info("Try to register users with session object id : {}", id);
        uploadLecturersService.registerUploadData(id);
        log.info("All users are registered");
    }

}
