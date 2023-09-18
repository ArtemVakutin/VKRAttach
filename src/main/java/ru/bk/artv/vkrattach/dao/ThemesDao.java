package ru.bk.artv.vkrattach.dao;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.ThemeRepository;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ThemesDao {

    ThemeRepository themeRepository;

    public ThemesDao(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> getThemesByDepartmentFacultyYear(String department, String faculty, String year) {
        try {
            List<Theme> themeList = themeRepository.getByDepartmentAndFacultyAndYearOfRecruitment(department, faculty, year);
            if (themeList.size() > 0) {
                return themeList;
            }
            throw new ResourceNotFoundException("Themes from Department : " + department +
                    "and Faculty : " + faculty + " and Year :" + year
                    + " are not found");
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No enum constant : " +
                    department + " : in ru.bk.artv.vkrattach.domain.Department");
        }
    }

    public List<Theme> getThemesByDepartmentFacultyYearNoException(String department, String faculty, String year) {
        return themeRepository.getByDepartmentAndFacultyAndYearOfRecruitment(department, faculty, year);
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

    public void deleteThemes(String department, String faculty, String year) {
        themeRepository.deleteByDepartmentAndFacultyAndYearOfRecruitment(department, faculty, year);
    }
}
