package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.services.model.Lecturer;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long>, JpaSpecificationExecutor<Lecturer> {
    List<Lecturer> getLecturerByDepartment(String department);

    List<Lecturer> getLecturerByDepartmentAndPosition(String department, String position);

    boolean existsBySurnameAndNameAndPatronymicAndDepartment(String surname, String name,
                                                             String patronymic, String department);
}
