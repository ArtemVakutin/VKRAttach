package ru.bk.artv.vkrattach.domain.dto;

import lombok.Data;
import ru.bk.artv.vkrattach.domain.Role;

import java.time.LocalDateTime;

@Data
public class UserRegistrationDTO {
    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private String password;
    private String telephone;
    private String faculty;
    private String group;
    private String yearOfRecruitment;
}
