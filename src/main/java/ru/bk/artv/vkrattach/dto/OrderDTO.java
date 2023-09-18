package ru.bk.artv.vkrattach.dto;

import lombok.Data;
import ru.bk.artv.vkrattach.domain.Order.OrderStatus;

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
    private OrderStatus requestStatus;
    private String department;
    private String comment;

}
