package ru.bk.artv.vkrattach.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.bk.artv.vkrattach.domain.user.Role;
import ru.bk.artv.vkrattach.services.validators.PasswordConstraint;

@Data
@JsonIgnoreProperties(value = {"password", "oldPassword"}, allowSetters = true)
public class UserDTO {

    public interface ValidationForRegisterUser {
        //validation marker interface
    }

    public interface ValidationForRegisterAdmin {
        //validation marker interface
    }

    public interface ValidationForRegisterModerator {
        //validation marker interface
    }

    public interface ValidationPassword {
        //validation marker interface
    }

    public interface ValidationId {
        //validation marker interface
    }

    @NotNull(groups = {ValidationId.class})
    private Long id;

    @NotBlank(groups = {ValidationId.class,
            ValidationForRegisterUser.class,
            ValidationForRegisterAdmin.class,
            ValidationForRegisterModerator.class}, message = "Поле 'Логин' не должно быть пустым")
    private String login;


    @NotBlank(groups = {ValidationForRegisterUser.class}, message = "Поле 'Фамилия' не должно быть пустым")
    private String surname;

    @NotBlank(groups = {ValidationForRegisterUser.class}, message = "Поле 'Имя' не должно быть пустым")
    private String name;

    private String patronymic = "";

    private String email;

    private String oldPassword;

    @PasswordConstraint(groups = {ValidationPassword.class}, message = "Поле 'Пароль' должно быть не менее 6 знаков," +
            "состоять из цифр, символов английского алфавита или знаков @#$%^&+=")
    private String password;

    @NotBlank(groups = {ValidationForRegisterUser.class}, message = "Выберите факультет")
    private String faculty;

    @NotBlank(groups = {ValidationForRegisterUser.class}, message = "Выберите год набора")
    private String year;

    private String telephone;

    private String group;

    @NotBlank(groups = {ValidationForRegisterModerator.class}, message = "Выберите кафедру")
    private String department;

    //only for uploading answer
    //только для ответа на загрузку таблицами
    private String exception;

    private Role role = Role.USER;
}
