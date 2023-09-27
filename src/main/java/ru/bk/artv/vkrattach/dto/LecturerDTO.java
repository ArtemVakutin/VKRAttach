package ru.bk.artv.vkrattach.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

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

    private String academicDegree;

    private String academicTitle;

    @NotBlank(message = "Выберите кафедру")
    private String department;

    private String rank;

    private String exception;

}

