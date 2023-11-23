package ru.bk.artv.vkrattach.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LecturerDto {

    private Long id;

    @NotBlank(message = "Поле \"Имя\" не должно быть пустым")
    private String name;

    @NotBlank(message = "Поле \"Фамилия\" не должно быть пустым")
    private String surname;

    private String patronymic;

    @NotBlank(message = "Выберите кафедру")
    private String department;
}
