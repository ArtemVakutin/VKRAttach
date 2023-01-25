package ru.bk.artv.vkrattach.domain.dto;

import lombok.Data;

@Data
public class OrderDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String group;
    private Long themeId;
    private String themeName;
    private Long lecturerId;
    private String lecturerName;
    private Integer requestStatus;
    private String department;
    private String comment;

}
