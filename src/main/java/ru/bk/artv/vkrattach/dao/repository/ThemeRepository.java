package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.Department;
import ru.bk.artv.vkrattach.domain.Theme;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> getByThemeDepartmentAndFaculty(Department department, String faculty);
}
