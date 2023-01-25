package ru.bk.artv.vkrattach.domain.dto;

import lombok.Data;
import ru.bk.artv.vkrattach.domain.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserToPatchDTO {

    private Long id;
    @NotBlank
    private String login;
    @NotBlank
    private String surname;
    @NotBlank
    private String name;
    private String patronymic;
    @NotBlank
    private String email;
    private String oldPassword;
    @NotBlank
    private String password;
    @NotBlank
    private String telephone;
    @NotBlank
    private String faculty;
    @NotBlank
    private String group;
    @NotBlank
    private String yearOfRecruitment;
    private Role role = Role.USER;
}
