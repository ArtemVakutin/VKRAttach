package ru.bk.artv.vkrattach.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.services.mappers.ThemesMapper;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.web.dto.ThemeDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CRUD - сервис для тем вкр (Theme) плюс получение списков тем.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ThemesService {

    ThemesDao themesDao;
    ThemesMapper themesMapper;
    OrderDao orderDao;


    /**
     * Возвращает список не занятых тем по году набора, специальности и кафедре
     *
     * @param department кафедра
     * @param faculty    специальность
     * @param year       год набора
     * @return список НЕ ЗАНЯТЫХ тем
     */
    @Transactional
    public List<ThemeDto> getThemes(String department, String faculty, String year) {
        List<Theme> themesList = themesDao.getThemesByDepartmentFacultyYear(department, faculty, year);
        List<ThemeDto> themesDtoList = themesList.stream()
                .map(theme -> themesMapper.themeToThemeDTO(theme))
                .collect(Collectors.toList());
        return themesDtoList;
    }

    /**
     * Возвращает список ВСЕХ (включая занятые) по спецификациям (год набора и т.д.).
     * Transactional нужен для подсчета закрепленных за темами работ.
     *
     * @param spec спецификации
     * @return список тем в Dto
     */
    @Transactional
    public List<ThemeDto> getAllThemes(Specification<Theme> spec) {
        List<Theme> themesList = themesDao.getAllThemes(spec);
        return themesList.stream()
                .map(theme -> themesMapper.themeToThemeDTO(theme))
                .collect(Collectors.toList());
    }

    /**
     * Удаляет тему
     *
     * @param id айдишник удаляемой темы
     */
    public void deleteTheme(Long id) {
        Theme theme = themesDao.getThemeById(id)
                .orElseThrow(()->new ResourceNotFoundException("Темы с таким id не существует"));
        if (orderDao.isOrderExists(theme)) {
            throw new ResourceNotSavedException("Невозможно удалить тему, которая уже закреплена за заявками," +
                    " нажмите на редактировать заявку и удалите заявку или измените тему");
        }
        themesDao.deleteTheme(id);
    }

    /**
     * Добавляет тему по одной
     *
     * @param themeDTO тема
     * @return добавленную тему в Dto
     */
    public ThemeDto addTheme(ThemeDto themeDTO) {
        Theme theme = themesMapper.toTheme(themeDTO);
        checkThemeIsExist(theme);
        themesDao.addTheme(theme);
        return themesMapper.themeToThemeDTO(theme);
    }

    /**
     * Изменяет тему по одной
     *
     * @param themeDTO тема
     * @return отредактированная тема в Dto
     */
    public ThemeDto patchTheme(ThemeDto themeDTO) {
        Theme theme = themesDao.getThemeById(themeDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme with id :" + themeDTO.getId() + " is not found"));
        themesMapper.toTheme(theme, themeDTO);
        checkThemeIsExist(theme);
        themesDao.addTheme(theme);
        return themesMapper.themeToThemeDTO(theme);
    }

    /**
     * Проверяет существование темы. В случае, если тема имеется, выбрасывается ошибка
     *
     * @param theme Тема
     */
    private void checkThemeIsExist(Theme theme) {
        if (themesDao.isThemeExist(theme)) {
            throw new ResourceNotSavedException("Вы пытаетесь сохранить уже существующую тему или при редактировании не внесли изменений");
        }
    }

    /**
     * Удаляет все темы по параметрам
     *
     * @param department кафедра
     * @param faculty    факультет
     * @param year       год набора
     * @return список тем, которые нельзя удалить в связи с наличием заявок на эти темы.
     */
    @Transactional
    public List<ThemeDto> deleteThemes(String department, String faculty, String year) {
        List<ThemeDto> themes = themesDao.getThemesByDepartmentFacultyYear(department, faculty, year)
                .stream()
                .filter(theme -> orderDao.isOrderExists(theme))
                .map(theme -> themesMapper.themeToThemeDTO(theme))
                .collect(Collectors.toList());
        if (!themes.isEmpty()) {
            return themes;
        }
        themesDao.deleteThemes(department, faculty, year);
        return themes;
    }
}
