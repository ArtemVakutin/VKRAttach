package ru.bk.artv.vkrattach.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.domain.*;
import ru.bk.artv.vkrattach.domain.dto.OrderDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    OrderDao orderDao;
    Departments departments;

    public OrderService(OrderDao orderDao, Departments departments) {
        this.orderDao = orderDao;
        this.departments = departments;
    }

    public Map<String, String> getDepartments() {
        return departments.getDepartmentsMap();
    }

    public Map<String, String> getLecturers(String department) {
        List<Lecturer> lecturersByDepartment = orderDao.getLecturersByDepartment(department);
        Map<String, String> lecturersMap = lecturersByDepartment.stream().collect(Collectors.toMap(item -> item.getId().toString(), item -> {
            return item.getSurname() + " " + item.getName() + " " + item.getPatronymic();
        }));
        return lecturersMap;
    }

    public Map<String, String> getThemes(String department, String faculty, String year) {
        List<Theme> themesList = orderDao.getThemesByDepartmentFacultyYear(department, faculty, year);
        Map<String, String> themes = themesList.stream().collect(Collectors.toMap(item -> item.getThemeId().toString(),
                item -> item.getThemeName()));
        return themes;
    }


    @Transactional
    public void addOrder(OrderDTO orderDTO, User user) {
        Order order = new Order();
        Theme theme = orderDao.getThemeById(orderDTO.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme with id :" +
                        orderDTO.getThemeId() + " and user :" + user.getId() + " " + user.getEmail() + " not found"));
        order.setUser(user);
        order.setTheme(theme);
        order.setRequestDate(LocalDate.now());
        order.setOrderStatus(Order.OrderStatus.UNDER_CONSIDERATION);
        order.setComments(orderDTO.getComment());
        orderDao.addOrder(order);
        orderDTO.setId(order.getId());
    }

    @Transactional
    public Map<String, OrderDTO> getOrders(User user) {
        List<Order> orders = orderDao.getOrdersByUser(user);
        Map<String, OrderDTO> orderMap = orders.stream().collect(Collectors.toMap(order -> order.getId().toString(),
                order -> {
                    OrderDTO orderDTO = orderToDTO(order);
                    return orderDTO;
                }));
        return orderMap;
    }

    private OrderDTO orderToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        if (order.getLecturer() != null) {
            orderDTO.setLecturerId(order.getLecturer().getId());
            orderDTO.setLecturerName(order.getLecturer().getSurname() + " "
                    + order.getLecturer().getName() + " "
                    + order.getLecturer().getPatronymic());
        }
        if (order.getTheme() != null) {
            orderDTO.setThemeId(order.getTheme().getThemeId());
            orderDTO.setThemeName(order.getTheme().getThemeName());
            orderDTO.setDepartment(order.getTheme().getThemeDepartment().name());
            orderDTO.setComment(order.getComments());
        }
        orderDTO.setRequestStatus(order.getOrderStatus().ordinal());
        return orderDTO;
    }

    public void deleteOrders(Long id, User user) {
        if (orderDao.isOrderExistsByIdAndUser(id, user)) {
            orderDao.deleteOrder(id);
        } else {
            throw new ResourceNotFoundException("Order with id :" + id
                    + " " + "and user :"
                    + user.getId() + "is not found");
        }


    }
}
