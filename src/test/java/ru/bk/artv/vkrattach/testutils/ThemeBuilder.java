package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.model.Theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeBuilder {

    private Long themeId = null;
    private String themeName = "Перспективы развития науки уголовного права.";
    private String department = "УПв";
    private String faculty = "НБФЗОП";
    private String year = "2019";
    private List<Order> orders = new ArrayList<>();

    private ThemeBuilder() {
        // Конструктор приватный, так как мы хотим, чтобы объект создавался только через статический метод create()
    }

    // Мы используем метод create(), чтобы создать экземпляр этого Builder
    public static ThemeBuilder create() {
        return new ThemeBuilder();
    }

    // Методы для установки значений полей
    public ThemeBuilder withThemeId(Long themeId) {
        this.themeId = themeId;
        return this;
    }

    public ThemeBuilder withThemeName(String themeName) {
        this.themeName = themeName;
        return this;
    }

    public ThemeBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public ThemeBuilder withFaculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    public ThemeBuilder withYear(String year) {
        this.year = year;
        return this;
    }

    public ThemeBuilder withOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public ThemeBuilder withOrder(Order order) {
        this.orders.add(order);
        return this;
    }

    // Метод build() для создания объекта Theme с установленными значениями полей
    public Theme build() {
        Theme theme = new Theme();
        theme.setThemeId(themeId);
        theme.setThemeName(themeName);
        theme.setDepartment(department);
        theme.setFaculty(faculty);
        theme.setYear(year);
        theme.setOrders(orders);

        return theme;
    }
}