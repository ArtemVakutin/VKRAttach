package ru.bk.artv.vkrattach.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.services.mappers.ThemesMapper;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.dto.ThemeDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ThemesService {

    ThemesDao themesDao;
    ThemesMapper themesMapper;
    OrderDao orderDao;

    @Transactional
    public List<ThemeDTO> getThemes(String department, String faculty, String year) {
        List<Theme> themesList = themesDao.getThemesByDepartmentFacultyYear(department, faculty, year);
//        themesList.stream().forEach((theme -> Hibernate.initialize(theme.getOrders())));
        List<ThemeDTO> themesDTO = themesList.stream()
                .map(theme -> themesMapper.themeToThemeDTO(theme))
                .filter(themeDTO -> !themeDTO.isBusy())
                .collect(Collectors.toList());
        return themesDTO;
    }

    @Transactional
    public List<ThemeDTO> getAllThemes(Specification<Theme> spec) {
        List<Theme> themesList = themesDao.getAllThemes(spec);
//        themesList.stream().forEach((theme -> Hibernate.initialize(theme.getOrders())));
        List<ThemeDTO> themesDTO = themesList.stream()
                .map(theme -> themesMapper.themeToThemeDTO(theme))
                .collect(Collectors.toList());
        return themesDTO;
    }

    public void deleteTheme(Long id) {
        themesDao.deleteTheme(id);
    }

    public ThemeDTO addTheme(ThemeDTO themeDTO) {
        Theme theme = new Theme();
        themesMapper.toTheme(theme, themeDTO);
        themesDao.addTheme(theme);
        themesMapper.themeToThemeDTO(theme);
        return themesMapper.themeToThemeDTO(theme);
    }

    public ThemeDTO patchTheme(ThemeDTO themeDTO) {
        Theme theme = themesDao.getThemeById(themeDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Theme with id :" +
                themeDTO.getId() + " is not found"));
        themesMapper.toTheme(theme, themeDTO);
        themesDao.addTheme(theme);
        return themesMapper.themeToThemeDTO(theme);
    }

    @Transactional
    public List<ThemeDTO> deleteThemes(String department, String faculty, String year) {
        List<ThemeDTO> themes = themesDao.getThemesByDepartmentFacultyYear(department, faculty, year)
                .stream()
                .filter(theme -> orderDao.isOrderExistsByTheme(theme))
                .map(theme -> themesMapper.themeToThemeDTO(theme))
                .collect(Collectors.toList());
        if (!themes.isEmpty()) {
            return themes;
        }
        themesDao.deleteThemes(department, faculty, year);
        return themes;
    }
}
