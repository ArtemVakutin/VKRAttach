package ru.bk.artv.vkrattach.dao;


import org.springframework.stereotype.Component;
import ru.bk.artv.vkrattach.dao.repository.LecturerRepository;
import ru.bk.artv.vkrattach.services.model.Lecturer;

import java.util.List;

@Component
public class DepartmentHeadGetterImpl implements DepartmentHeadGetter{

    LecturerRepository lecturerRepository;
    public final static String DEPARTMENT_HEAD_POSITION = "начальник кафедры";
    public final static Lecturer DEPARTMENT_HEAD_ABSTRACT = new Lecturer();

    public DepartmentHeadGetterImpl(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    static {
        DEPARTMENT_HEAD_ABSTRACT.setDepartment("");
        DEPARTMENT_HEAD_ABSTRACT.setName("ИМЯ");
        DEPARTMENT_HEAD_ABSTRACT.setSurname("ФАМИЛИЯ");
        DEPARTMENT_HEAD_ABSTRACT.setPatronymic("ОТЧЕСТВО");
        DEPARTMENT_HEAD_ABSTRACT.setEmail("nach@kaf.ru");
        DEPARTMENT_HEAD_ABSTRACT.setTelephone("02");
        DEPARTMENT_HEAD_ABSTRACT.setRank("звание");
        DEPARTMENT_HEAD_ABSTRACT.setAcademicDegree("ученая степень");
        DEPARTMENT_HEAD_ABSTRACT.setAcademicDegree("ученое звание");
        DEPARTMENT_HEAD_ABSTRACT.setPosition("начальник кафедры");
    }

    /**
     * Метод ищет начальника кафедры в базе, если не находит, возвращает DEPARTMENT_HEAD_ABSTRACT
     * При этом поля начальника кафедры проверяются на незаполненность и в случае чего заполняются значениями
     * из DEPARTMENT_HEAD_ABSTRACT. Внимание! Значения не совпадают с конфигом, используются только для генерации
     * документов.
     *
     * @param department название кафедры
     * @return Lecturer, либо из базы, либо DEPARTMENT_HEAD_ABSTRACT, если в базе нет
     */
    @Override
    public Lecturer getDepartmentHead(String department) {
        List<Lecturer> departmentHeads = lecturerRepository.getLecturerByDepartmentAndPosition(department, DEPARTMENT_HEAD_POSITION);
        Lecturer lecturer = departmentHeads.stream().findFirst().orElse(DEPARTMENT_HEAD_ABSTRACT);

        lecturer.setDepartment(department);
        lecturer.setRank(lecturer.getRank().equals("") ? DEPARTMENT_HEAD_ABSTRACT.getRank() : lecturer.getRank());
        lecturer.setAcademicDegree(lecturer.getAcademicDegree().equals("") ? DEPARTMENT_HEAD_ABSTRACT.getAcademicDegree() : lecturer.getAcademicDegree());
        lecturer.setAcademicTitle(lecturer.getRank().equals("") ? DEPARTMENT_HEAD_ABSTRACT.getAcademicTitle() : lecturer.getAcademicTitle());

        return lecturer;
    }

}
