package ru.bk.artv.vkrattach.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.web.dto.OrderDTO;
import ru.bk.artv.vkrattach.exceptions.OrderAcceptedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.mappers.OrderMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CRUD - сервис для заявок (Order) плюс получение списков order.
 */
@Slf4j
@Service
public class OrderService {

    OrderDao orderDao;
    OrderMapper orderMapper;
    UserDao userDao;
    ThemesDao themesDao;

    public OrderService(UserDao userDao, OrderDao orderDao, OrderMapper orderMapper, ThemesDao themesDao) {
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
        this.themesDao = themesDao;
        this.userDao = userDao;
    }


    /**
     * @param id айдишник пользователя. Для модераторов и админов
     * @return все заявки для конкретного пользователя в Dto
     */
    @Transactional
    public List<OrderDTO> getOrders(Long id) {
        return getOrdersByUser((SimpleUser) userDao.findUserById(id));
    }

    /**
     * @param user аутентифицированный пользователь. Для User-ов
     * @return все заявки для конкретного пользователя в Dto
     */
    @Transactional
    public List<OrderDTO> getOrders(SimpleUser user) {
        return getOrdersByUser(user);
    }

    private List<OrderDTO> getOrdersByUser(SimpleUser user) {
        return orderDao.getOrdersByUser(user)
                .stream()
                .map(order -> orderMapper.orderToDTO(order))
                .collect(Collectors.toList());
    }

    /**
     * @param orderDTO добавляемая заявка
     * @return добавленную заявку в Dto
     */
    @Transactional
    public OrderDTO addOrderByAdmin(OrderDTO orderDTO) {
        SimpleUser userById = (SimpleUser) userDao.findUserById(orderDTO.getUserId());
        return addOrder(orderDTO, userById);
    }

    /**
     * @param orderDTO добавляемая пользователем заявка
     * @param user     аутентифицированный пользователь
     * @return сохраненную заявку
     */
    @Transactional
    public OrderDTO addOrderByUser(OrderDTO orderDTO, SimpleUser user) {
        return addOrder(orderDTO, user);
    }

    // добавляет заявку в базу данных. Проверяет на наличие принятых или отказанных заявок
    private OrderDTO addOrder(OrderDTO orderDTO, SimpleUser user) {
        try {
            orderDao.getOrdersByUser(user).forEach(order -> {
                if (order.getRequestStatus() == Order.RequestStatus.ACCEPTED) {
                    throw new OrderAcceptedException("User with id : " + user.getId() + " already has accepted order");
                } else if (order.getRequestStatus() == Order.RequestStatus.UNDER_CONSIDERATION) {
                    throw new OrderAcceptedException("User with id : " + user.getId() + " already has non refused order");
                }
            });
        } catch (
                ResourceNotFoundException exception) { // дабы не выскакивала ошибка при отсутствии заявок у пользователя
        }
        Order order = new Order();
        order.setRequestStatus(Order.RequestStatus.UNDER_CONSIDERATION);
        order.setUser(user);
        orderMapper.DTOtoOrder(orderDTO, order);
        order.setRequestDate(LocalDate.now());
        orderDao.addOrder(order);
        return orderMapper.orderToDTO(order);
    }

    /**
     * Удаляет заявку.
     *
     * @param id   айдишник пользователя (для админа)
     */
    public void deleteOrders(Long id) {
        orderDao.deleteOrder(id);
    }

    /**
     * Удаляет заявку. Если пользователь - USER, то проверяет заявку на статус (удаляет, только если UNDER_CONSIDERATION)
     *
     * @param id   айдишник пользователя (для админа)
     * @param user аутентифицированный пользвователь
     */
    public void deleteOrders(Long id, DefaultUser user) {
        Order order = orderDao.getOrder(id, (SimpleUser) user)
                .orElseThrow(() -> new ResourceNotFoundException("order with id : " + id + " is not found, you cant delete it"));

        if (order.getRequestStatus().equals(Order.RequestStatus.ACCEPTED) ||
                order.getRequestStatus().equals(Order.RequestStatus.REFUSED)) {
            throw new ResourceNotFoundException("Order with id :" + id
                    + " " + "and user :"
                    + user.getId() + "is not found");
        }
        orderDao.deleteOrder(id);
    }

    /**
     * Изменяет Order. Устанавливает дату рассмотрения заявки в случае несовпадения статусов.
     *
     * @param orderDTO заявка для изменения
     */
    @Transactional
    public void patchOrder(OrderDTO orderDTO) {
        final Long id = orderDTO.getId();
        Order order = orderDao.getOrder(orderDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + id + " is not found"));
        checkPatchingOrder(orderDTO, order);
        if (orderDTO.getRequestStatus() != order.getRequestStatus()) {
            order.setRequestStatusDate(LocalDate.now());
        }
        orderMapper.DTOtoOrder(orderDTO, order);
        orderDao.addOrder(order);
        orderMapper.orderToDTO(order, orderDTO);
    }

    //проверяет на Null поле lecturer (руководитель ВКР). При рассмотренной заявке оно должно быть не null и наоборот.
    private void checkPatchingOrder(OrderDTO orderDTO, Order order) {

        if (orderDTO.getRequestStatus() == Order.RequestStatus.ACCEPTED && orderDTO.getLecturerId() == null) {
            orderDTO.setRequestStatus(Order.RequestStatus.UNDER_CONSIDERATION);
            return;
        }
        if (orderDTO.getRequestStatus() != Order.RequestStatus.ACCEPTED
                && order.getRequestStatus() == Order.RequestStatus.ACCEPTED) {
            orderDTO.setLecturerId(null);
            return;
        }
        if (orderDTO.getLecturerId() != null) {
            orderDTO.setRequestStatus(Order.RequestStatus.ACCEPTED);
        }
    }

    /**
     * @param spec спецификации (год набора, кафедра и т.д.)
     * @return список заявок в Dto
     */
    public List<OrderDTO> getOrders(Specification<Order> spec) {
        return orderDao.getOrders(spec)
                .stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderMapper.orderToDTO(order, orderDTO);
                    return orderDTO;
                })
                .collect(Collectors.toList());

    }
}
