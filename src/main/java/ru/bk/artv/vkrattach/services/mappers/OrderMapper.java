package ru.bk.artv.vkrattach.services.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.services.model.Lecturer;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.web.dto.OrderDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OrderMapper {

    ThemesDao themesDao;
    LecturerDao lecturerDao;

    public void DTOtoOrder(OrderDTO orderDTO, Order order) {
        log.info(orderDTO.toString());
        log.info(order.toString());
        if (orderDTO.getThemeId() != null) {

            Theme theme = themesDao.getThemeById(orderDTO.getThemeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Theme with id :" +
                            orderDTO.getThemeId() + " is not found"));
            List<Order> orders = theme.getOrders().stream().filter((themeOrder -> themeOrder.getRequestStatus() != Order.RequestStatus.REFUSED)).collect(Collectors.toList());
            if (orders.size() > 0 && (order.getTheme()==null || !order.getTheme().getThemeId().equals(orderDTO.getThemeId()))) {
                throw new ResourceNotFoundException("theme with id: " + theme.getThemeId() + " is busy by : " + theme.getOrders().get(0).getUser().getId());
            } else {
                order.setTheme(theme);
            }
        }

        if (orderDTO.getLecturerId() != null) {
            Lecturer lecturer = lecturerDao.getLecturerById(orderDTO.getLecturerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lecturer with id : " + orderDTO.getLecturerId() + " is not found"));
            order.setLecturer(lecturer);
        } else {
            order.setLecturer(null);
        }

        if (orderDTO.getRequestStatus() != null) {
            order.setRequestStatus(orderDTO.getRequestStatus());
        }

        if (orderDTO.getComment() != null) {
            order.setComments(orderDTO.getComment());
        }
    }

    public OrderDTO orderToDTO(Order order, OrderDTO orderDTO) {
        orderToDtoMapper(order, orderDTO);
        return orderDTO;
    }
    public OrderDTO orderToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderToDtoMapper(order, orderDTO);
        return orderDTO;
    }
    private void orderToDtoMapper(Order order, OrderDTO orderDTO) {

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
            orderDTO.setDepartment(order.getTheme().getDepartment());
            orderDTO.setComment(order.getComments());
        }

        orderDTO.setRequestStatus(order.getRequestStatus());
    }

}
