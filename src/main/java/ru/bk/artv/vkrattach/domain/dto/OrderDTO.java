package ru.bk.artv.vkrattach.domain.dto;

import lombok.Data;
import ru.bk.artv.vkrattach.domain.Theme;

@Data
public class OrderDTO {

    private Long id;
    private Long themeId;
    private Long preferredLecturerId;
    private Long lecturerId;
    private Boolean requestAccept;
    private String department;

}
