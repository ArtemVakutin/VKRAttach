package ru.bk.artv.vkrattach.web.dto;

import lombok.Data;
import ru.bk.artv.vkrattach.services.model.Order.RequestStatus;

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
    private RequestStatus requestStatus;
    private String department;
    private String comment;

}
