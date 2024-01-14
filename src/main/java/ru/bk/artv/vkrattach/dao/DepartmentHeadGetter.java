package ru.bk.artv.vkrattach.dao;

import ru.bk.artv.vkrattach.services.model.Lecturer;

/**
 * Для поиска Lecturer по названию кафедры. Как правило для начальников кафедр
 */
@FunctionalInterface
public interface DepartmentHeadGetter {
    public Lecturer getDepartmentHead(String department);
}
