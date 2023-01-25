package ru.bk.artv.vkrattach.business.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.domain.DepartmentsMap;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.domain.dto.OrderDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

@Service
@AllArgsConstructor
public class OrderMapper {

    ThemesDao themesDao;
    LecturerDao lecturerDao;

    public void DTOtoOrder(OrderDTO orderDTO, Order order) {
        if (orderDTO.getThemeId() != null && orderDTO.getThemeId() != order.getTheme().getThemeId()) {
            Theme theme = themesDao.getThemeById(orderDTO.getThemeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theme with id : " + orderDTO.getThemeId() + " is not found"));
            order.setTheme(theme);
        }
        if (orderDTO.getLecturerId() != null) {
            Lecturer lecturer = lecturerDao.getLecturerById(orderDTO.getLecturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer with id : " + orderDTO.getLecturerId() + " is not found"));
            order.setLecturer(lecturer);
        } else {
            order.setLecturer(null);
        }

        if (orderDTO.getRequestStatus() != null) {
            Order.OrderStatus value = Order.OrderStatus.values()[orderDTO.getRequestStatus()];
            order.setOrderStatus(value);
        }
        if (orderDTO.getComment() != null) {
            order.setComments(orderDTO.getComment());
        }
    }

    public void orderToDTO(Order order, OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        if (order.getUser() != null) {
            orderDTO.setUserId(order.getUser().getId());
            orderDTO.setUserName(order.getUser().getSurname() + " "
                    + order.getUser().getName() + " "
                    + order.getUser().getPatronymic());
            orderDTO.setGroup(order.getUser().getFaculty() + "-"
                    + order.getUser().getGroup());
        }
        if (order.getLecturer() != null) {
            orderDTO.setLecturerId(order.getLecturer().getId());
            orderDTO.setLecturerName(order.getLecturer().getSurname() + " "
                    + order.getLecturer().getName() + " "
                    + order.getLecturer().getPatronymic());
        }
        if (order.getTheme() != null) {
            orderDTO.setThemeId(order.getTheme().getThemeId());
            orderDTO.setThemeName(order.getTheme().getThemeName());
            orderDTO.setDepartment(order.getTheme().getThemeDepartment());
            orderDTO.setComment(order.getComments());
        }
        orderDTO.setRequestStatus(order.getOrderStatus().ordinal());
    }
}
