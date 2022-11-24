package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.Department;
import ru.bk.artv.vkrattach.domain.Lecturer;

import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    List<Lecturer> getLecturerByDepartment(Department department);
}
