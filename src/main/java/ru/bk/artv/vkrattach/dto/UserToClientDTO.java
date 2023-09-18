package ru.bk.artv.vkrattach.dto;

import lombok.Data;
import ru.bk.artv.vkrattach.domain.Role;

import java.time.LocalDateTime;

@Data
public class UserToClientDTO {
    private Long id;
    private String login;
    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private String telephone;
    private String faculty;
    private String group;
    private String yearOfRecruitment;
    private Role role;
}
