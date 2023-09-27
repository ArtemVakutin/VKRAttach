package ru.bk.artv.vkrattach.services.comparators;

import ru.bk.artv.vkrattach.domain.Order;

import java.util.Comparator;

public class OrdersComparatorByDepartmentLecturerTheme implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        int byDepartment = o1.getTheme().getDepartment().compareTo(o2.getTheme().getDepartment());

        if (byDepartment != 0) {
            return byDepartment;
        }

        int byLecturer = o1.getLecturer().getId().compareTo(o2.getLecturer().getId());

        if (byLecturer != 0) {
            return byLecturer;
        }

        return o1.getTheme().getThemeId().compareTo(o2.getTheme().getThemeId());
    }
}
