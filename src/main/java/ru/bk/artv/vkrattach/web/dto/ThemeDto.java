package ru.bk.artv.vkrattach.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ThemeDto {
    private Long id;
    @NotBlank(message = "Название темы не должно быть пустым")
    private String themeName;
    @NotBlank(message = "Название кафедры не должно быть пустым")
    private String department;
    @NotBlank(message = "Название специальности (направления подготовки не должно быть пустым")
    private String faculty;
    @NotBlank(message = "Год набора не должен быть пустой")
    private String year;
    private boolean isBusy;

}
