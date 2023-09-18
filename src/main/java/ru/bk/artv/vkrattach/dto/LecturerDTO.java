package ru.bk.artv.vkrattach.dto;

import lombok.Data;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.domain.Lecturer.AcademicDegree;
import ru.bk.artv.vkrattach.domain.Lecturer.AcademicTitle;
import ru.bk.artv.vkrattach.domain.Rank;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LecturerDTO {


    private Long id;

    @NotBlank(message = "Поле \"Имя\" не должно быть пустым")
    private String name;

    @NotBlank(message = "Поле \"Фамилия\" не должно быть пустым")
    private String surname;

    private String patronymic;

    private String email;

    private String telephone;

    private AcademicDegree academicDegree;

    private AcademicTitle academicTitle;

    @NotBlank(message = "Выберите кафедру")
    private String department;

    private Rank rank;

    private String exception;

}

