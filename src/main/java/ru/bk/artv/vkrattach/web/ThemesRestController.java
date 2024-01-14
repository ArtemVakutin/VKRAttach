package ru.bk.artv.vkrattach.web;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.services.ThemesService;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.web.dto.ThemeDto;

import java.util.List;


/**
 * CRUD для тем, а также получение списков тем по разным параметрам.
 */
@RestController
@RequestMapping("/rest/theme")
public class ThemesRestController {

    //Сервис для работы с темами
    private final ThemesService themesService;

    public ThemesRestController(ThemesService themesService) {
        this.themesService = themesService;
    }

    //Добавить столбец к темам (забито не забито)

    /**
     * Возвращает список незанятых тем если notBusy true и все темы если notBusy false
     *
     * @param department кафедра
     * @param faculty    специальность
     * @param year       год набора
     * @param notBusy    надо ли возвращать только не занятые
     * @return список НЕЗАНЯТЫХ тем
     */
    @GetMapping(path = "themes")
    @PreAuthorize("isAuthenticated()")
    public List<ThemeDto> getThemes(@RequestParam("department") String department,
                                           @RequestParam("faculty") String faculty,
                                           @RequestParam("year") String year,
                                           @RequestParam(name = "notBusy", required = false, defaultValue = "true") boolean notBusy) {
        if (notBusy) {
            return themesService.getThemes(department, faculty, year).stream().filter(themeDto -> !themeDto.isBusy()).toList();
        }
        return themesService.getThemes(department, faculty, year);
    }

    /**
     * Удаляет ВСЕ темы по параметрам если нет закрепленных тем.
     *
     * @param department кафедра
     * @param faculty    специальность
     * @param year       год набора
     * @return список тем, которые не могут быть удалены в связи с наличием закрепленных за этими темами заявок
     */
    @DeleteMapping(path = "themes")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<ThemeDto> deleteThemes(@RequestParam("department") String department,
                                       @RequestParam("faculty") String faculty,
                                       @RequestParam("year") String year) {
        return themesService.deleteThemes(department, faculty, year);
    }


    /**
     * удаляет тему по айдишнику
     *
     * @param id айдишник темы
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void deleteTheme(@RequestParam("id") Long id) {
        themesService.deleteTheme(id);
    }

    /**
     * добавляет одну тему
     *
     * @param themeDTO добавляемая тема
     * @return добавленную тему
     */
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ThemeDto addTheme(@Valid @RequestBody ThemeDto themeDTO) {
        return themesService.addTheme(themeDTO);
    }

    /**
     * Изменяет одну тему
     *
     * @param themeDTO изменяемая тема
     * @return измененная тема
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ThemeDto patchTheme(@Valid @RequestBody ThemeDto themeDTO) {
        return themesService.patchTheme(themeDTO);
    }

}
