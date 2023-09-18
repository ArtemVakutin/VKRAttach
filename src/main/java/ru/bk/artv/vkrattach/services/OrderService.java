package ru.bk.artv.vkrattach.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.dto.OrderDTO;
import ru.bk.artv.vkrattach.exceptions.OrderAcceptedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.mappers.OrderMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public List<OrderDTO> getOrders(Long id, DefaultUser user) {
        if (user instanceof SimpleUser) {
            return orderDao.getOrdersByUser((SimpleUser) user)
                    .stream()
                    .map(order -> {
                        OrderDTO orderDTO = new OrderDTO();
                        orderMapper.orderToDTO(order, orderDTO);
                        return orderDTO;
                    }).collect(Collectors.toList());

        }
        SimpleUser userById = (SimpleUser)userDao.findUserById(id);
        return orderDao.getOrdersByUser(userById)
                .stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderMapper.orderToDTO(order, orderDTO);
                    return orderDTO;
                }).collect(Collectors.toList());
    }

    @Transactional
    public void addOrderByAdmin(OrderDTO orderDTO) {
        SimpleUser userById = (SimpleUser) userDao.findUserById(orderDTO.getUserId());
        addOrder(orderDTO, userById);
    }

    @Transactional
    public void addOrderByUser(OrderDTO orderDTO, SimpleUser user) {
        addOrder(orderDTO, user);
    }

    private void addOrder(OrderDTO orderDTO, SimpleUser user) {
        log.info(orderDTO.toString());
        try {
            orderDao.getOrdersByUser(user).stream().forEach(order -> {
                if (order.getOrderStatus() == Order.OrderStatus.ACCEPTED) {
                    throw new OrderAcceptedException("User with id : " + user.getId() + " already has accepted order");
                } else if (order.getOrderStatus() != Order.OrderStatus.REFUSED) {
                    throw new OrderAcceptedException("User with id : " + user.getId() + " already has non refused order");
                }
            });
        } catch (ResourceNotFoundException exception) {

        }

        Order order = new Order();
        order.setOrderStatus(Order.OrderStatus.UNDER_CONSIDERATION);
        order.setUser(user);
        orderMapper.DTOtoOrder(orderDTO, order);
        order.setRequestDate(LocalDate.now());
        orderDao.addOrder(order);
        orderMapper.orderToDTO(order, orderDTO);
        log.info("После метода");
        log.info(order.toString());
        log.info(orderDTO.toString());
    }

    public void deleteOrders(Long id, DefaultUser user) {
        if (user.getRole().equals(Role.ADMIN)) {
            orderDao.deleteOrder(id);
        } else if (user.getRole().equals(Role.USER)) {
            if (orderDao.isOrderExistsByIdAndUser(id, (SimpleUser) user)) {
                orderDao.deleteOrder(id);
            } else {
                throw new ResourceNotFoundException("Order with id :" + id
                        + " " + "and user :"
                        + user.getId() + "is not found");
            }
        }
    }

    @Transactional
    public void patchOrder(OrderDTO orderDTO) {
        final Long id = orderDTO.getId();
        Order order = orderDao.getOrder(orderDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Order with id: " + id + " is not found"));
        orderMapper.DTOtoOrder(orderDTO, order);
        orderDao.addOrder(order);
        orderMapper.orderToDTO(order, orderDTO);
    }

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
