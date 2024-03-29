package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.services.model.Theme;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long>, JpaSpecificationExecutor<Theme> {
    List<Theme> getByDepartmentAndFacultyAndYear(String department, String faculty, String year);
    void deleteByDepartmentAndFacultyAndYear(String department, String faculty, String year);
    boolean existsByThemeNameAndDepartmentAndFacultyAndYear(String themeName, String department, String faculty, String year);

}
