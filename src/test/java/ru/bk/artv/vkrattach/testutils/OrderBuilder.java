package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.services.model.Lecturer;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;

import java.time.LocalDate;

public class OrderBuilder {

    private Long id = null;
    private Theme theme;
    private SimpleUser user;
    private Lecturer lecturer;
    private LocalDate requestDate = LocalDate.now();
    private Order.RequestStatus requestStatus = Order.RequestStatus.UNDER_CONSIDERATION;
    private LocalDate requestStatusDate = null;
    private String comments = "DefaultComments";

    private OrderBuilder() {
        // Конструктор приватный, так как мы хотим, чтобы объект создавался только через статический метод create()
    }

    // Мы используем метод create(), чтобы создать экземпляр этого Builder
    public static OrderBuilder create() {
        return new OrderBuilder();
    }

    // Методы для установки значений полей
    public OrderBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public OrderBuilder withTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public OrderBuilder withUser(SimpleUser user) {
        this.user = user;
        return this;
    }

    public OrderBuilder withLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public OrderBuilder withRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public OrderBuilder withRequestStatus(Order.RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        return this;
    }

    public OrderBuilder withRequestStatusDate(LocalDate requestStatusDate) {
        this.requestStatusDate = requestStatusDate;
        return this;
    }

    public OrderBuilder withComments(String comments) {
        this.comments = comments;
        return this;
    }

    // Метод build() для создания объекта Order с установленными значениями полей
    public Order build() {
        Order order = new Order();
        order.setId(id);
        order.setTheme(theme);
        order.setUser(user);
        order.setLecturer(lecturer);
        order.setRequestDate(requestDate);
        order.setRequestStatus(requestStatus);
        order.setRequestStatusDate(requestStatusDate);
        order.setComments(comments);

        return order;
    }
}