package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.services.model.Order.RequestStatus;
import ru.bk.artv.vkrattach.web.dto.OrderDTO;

public class OrderDtoBuilder {
    private Long id = null;
    private Long userId = 1L;
    private String userName = "DefaultUser";
    private String group = "DefaultGroup";
    private Long themeId = 2L;
    private String themeName = "DefaultTheme";
    private Long lecturerId = 3L;
    private String lecturerName = "DefaultLecturer";
    private RequestStatus requestStatus = RequestStatus.UNDER_CONSIDERATION;
    private String department = "DefaultDepartment";
    private String comment = "DefaultComment";

    private OrderDtoBuilder() {
    }

    public static OrderDtoBuilder create() {
        return new OrderDtoBuilder();
    }

    public OrderDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderDtoBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public OrderDtoBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public OrderDtoBuilder withGroup(String group) {
        this.group = group;
        return this;
    }

    public OrderDtoBuilder withThemeId(Long themeId) {
        this.themeId = themeId;
        return this;
    }

    public OrderDtoBuilder withThemeName(String themeName) {
        this.themeName = themeName;
        return this;
    }

    public OrderDtoBuilder withLecturerId(Long lecturerId) {
        this.lecturerId = lecturerId;
        return this;
    }

    public OrderDtoBuilder withLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
        return this;
    }

    public OrderDtoBuilder withRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public OrderDtoBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public OrderDtoBuilder withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public OrderDTO build() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        orderDTO.setUserId(userId);
        orderDTO.setUserName(userName);
        orderDTO.setGroup(group);
        orderDTO.setThemeId(themeId);
        orderDTO.setThemeName(themeName);
        orderDTO.setLecturerId(lecturerId);
        orderDTO.setLecturerName(lecturerName);
        orderDTO.setRequestStatus(requestStatus);
        orderDTO.setDepartment(department);
        orderDTO.setComment(comment);
        return orderDTO;
    }
}