package ru.bk.artv.vkrattach.domain.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String login;
    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private String oldPassword;
    private String password;
    private String telephone;
    private String faculty;
    private String group;
    private String yearOfRecruitment;
}
