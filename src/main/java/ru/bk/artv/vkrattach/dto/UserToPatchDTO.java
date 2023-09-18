package ru.bk.artv.vkrattach.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.bk.artv.vkrattach.services.validators.PasswordConstraint;
import ru.bk.artv.vkrattach.domain.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

@Data
@JsonIgnoreProperties(value = { "password" }, allowSetters = true)
public class UserToPatchDTO {

    public interface ValidationStepForRegister {
        //validation marker interface
    }

    public interface ValidationStepForPatch {
        //validation marker interface
    }

    @NotNull(groups = {UserToPatchDTO.ValidationStepForPatch.class})
    private Long id;

    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class,
            Default.class}, message = "Поле 'Логин' не должно быть пустым")
    private String login;


    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class,
            Default.class}, message = "Поле 'Фамилия' не должно быть пустым")
    private String surname;

    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class,
            Default.class}, message = "Поле 'Имя' не должно быть пустым")
    private String name;

    private String patronymic="";

    @Email(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class})
    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class}, message = "Поле 'E-mail' не должно быть пустым")
    private String email;

    private String oldPassword;

    @PasswordConstraint(groups = {Default.class})
    private String password;

    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class}, message = "Поле 'Телефон' не должно быть пустым")
    private String telephone;

    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class}, message = "Выберите факультет")
    private String faculty;

    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class}, message = "Поле 'Группа' не должно быть пустым")
    private String group;

    @NotBlank(groups = {UserToPatchDTO.ValidationStepForPatch.class,
            UserToPatchDTO.ValidationStepForRegister.class}, message = "Выберите год набора")
    private String yearOfRecruitment;

    //only for uploading answer
    //только для ответа на загрузку таблицами
    private String exception;

    private Role role = Role.USER;
}
