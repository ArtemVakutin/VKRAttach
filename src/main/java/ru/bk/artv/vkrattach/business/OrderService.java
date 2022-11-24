package ru.bk.artv.vkrattach.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.domain.*;
import ru.bk.artv.vkrattach.domain.dto.OrderDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    OrderDao orderDao;
    Departments departments;

    public OrderService(OrderDao orderDao, Departments departments) {
        this.orderDao = orderDao;
        this.departments = departments;
    }

    public HashMap<String, String> getDepartments(){
    return departments.getDepartmentsMap();

}

    public Map<Long, String> getLecturers(String department) {
        List<Lecturer> lecturersByDepartment = orderDao.getLecturersByDepartment(department);
        Map<Long, String> lecturersMap = lecturersByDepartment.stream().collect(Collectors.toMap(item -> item.getId(), item -> {
            return item.getSurname() + " " + item.getName() + " " + item.getPatronymic();
        }));

        return lecturersMap;
    }

    public Map<Long, String> getThemes(String department, String faculty) {
        List<Theme> themesList = orderDao.getThemesByDepartmentFaculty(department, faculty);
        Map<Long, String> themes = themesList.stream().collect(Collectors.toMap(item -> item.getThemeId(),
                item -> item.getThemeName()));

        return themes;
    }


    @Transactional
    public void addOrder (OrderDTO orderDTO, User user) {
        Order order = new Order();
        Theme theme = orderDao.getThemeById(orderDTO.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme with id :" +
                orderDTO.getThemeId() + " and user :" + user.getId() + " " + user.getEmail() + " not found"));
        Lecturer lecturer = orderDao.getLecturerById(orderDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer with id :" +
                orderDTO.getLecturerId() + " and user :" + user.getId() + " " + user.getEmail() + " not found"));
        Lecturer preferredLecturer = orderDao.getLecturerById(orderDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("preferredLecturer with id :" +
                        orderDTO.getLecturerId() + " and user :" + user.getId() + " " + user.getEmail() + " not found"));
        order.setUser(user);
        order.setTheme(theme);
        order.setLecturer(lecturer);
        order.setPreferredLecturer(preferredLecturer);
        orderDao.addOrder(order);
        orderDTO.setId(order.getId());
        orderDTO.setDepartment(order.getTheme().getThemeDepartment().name());
    }

}
