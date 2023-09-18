package ru.bk.artv.vkrattach.dto;

import lombok.Data;

@Data
public class ThemeDTO {
    private Long id;
    private String themeName;
    private String department;
    private String faculty;
    private String yearOfRecruitment;
    private boolean isBusy;
//    private OrderDTO order;

}
