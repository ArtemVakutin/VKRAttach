package ru.bk.artv.vkrattach.business;

import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.domain.Theme;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThemesService {

    ThemesDao themesDao;

    public ThemesService(ThemesDao themesDao) {
        this.themesDao = themesDao;
    }

    public Map<String, String> getThemes(String department, String faculty, String year) {
        List<Theme> themesList = themesDao.getThemesByDepartmentFacultyYear(department, faculty, year);
        Map<String, String> themes = themesList.stream().collect(Collectors.toMap(item -> item.getThemeId().toString(),
                item -> item.getThemeName()));
        return themes;
    }
}
