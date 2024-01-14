package ru.bk.artv.vkrattach.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.ThemeRepository;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Класс создан как промежуточное между сервисами и репозиториями Spring JPA.
 * В большинстве случаев просто перенаправляет в репозиторий.
 * В ряде случаев при отсутствии в списках тем выбрасывает ResourceNotFountExceptions
 */
@Service
public class ThemesDao {

    ThemeRepository themeRepository;

    public ThemesDao(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getThemesByDepartmentFacultyYear(String department, String faculty, String year) {
        try {
            List<Theme> themeList = themeRepository.getByDepartmentAndFacultyAndYear(department, faculty, year);
            if (themeList.size() > 0) {
                return themeList;
            }
            throw new ResourceNotFoundException("Themes from Department : " + department +
                    "and Faculty : " + faculty + " and Year :" + year
                    + " are not found");
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No such departments in config constant : " + department);
        }
    }

    public List<Theme> getThemesByDepartmentFacultyYearNoException(String department, String faculty, String year) {
        return themeRepository.getByDepartmentAndFacultyAndYear(department, faculty, year);
    }

    public Optional<Theme> getThemeById(Long id) {
        return themeRepository.findById(id);
    }

    public List<Theme> getAllThemes(Specification<Theme> spec) {
        return themeRepository.findAll(spec);
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }

    public void addTheme(Theme theme) {
        themeRepository.saveAndFlush(theme);
    }


    /**
     * Проверяет, имеется ли сохраненная тема с такими данными (не по айдишнику)
     *
     * @param theme тема, которую пытаются сохранить/внести изменения
     */
    public boolean isThemeExist(Theme theme) {
        return themeRepository.existsByThemeNameAndDepartmentAndFacultyAndYear(theme.getThemeName(),
                theme.getDepartment(),
                theme.getFaculty(),
                theme.getYear());
    }

    public void deleteThemes(String department, String faculty, String year) {
        themeRepository.deleteByDepartmentAndFacultyAndYear(department, faculty, year);
    }
}
