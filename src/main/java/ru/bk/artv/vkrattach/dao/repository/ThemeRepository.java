package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.Theme;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long>, JpaSpecificationExecutor<Theme> {
    List<Theme> getByThemeDepartmentAndFacultyAndYearOfRecruitment(String department, String faculty, String year);
}
