package ru.bk.artv.vkrattach.business;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.build.Plugin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.business.mappers.OrderMapper;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.domain.*;
import ru.bk.artv.vkrattach.domain.dto.OrderDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    OrderDao orderDao;
    OrderMapper orderMapper;
    ThemesDao themesDao;

    public OrderService(OrderDao orderDao, OrderMapper orderMapper, ThemesDao themesDao) {
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
        this.themesDao = themesDao;
    }

    @Transactional
    public void addOrder(OrderDTO orderDTO, SimpleUser user) {
        Order order = new Order();
        Theme theme = themesDao.getThemeById(orderDTO.getThemeId())
                .orElseThrow(() -> new ResourceNotFoundException("Theme with id :" +
                        orderDTO.getThemeId() + " and user :" + user.getId() + " " + user.getLogin() + " not found"));
        order.setUser(user);
        order.setTheme(theme);
        order.setRequestDate(LocalDate.now());
        order.setOrderStatus(Order.OrderStatus.UNDER_CONSIDERATION);
        order.setComments(orderDTO.getComment());
        orderDao.addOrder(order);
        orderDTO.setId(order.getId());
    }

    @Transactional
    public Map<Long, OrderDTO> getOrders(SimpleUser user) {
        List<Order> orders = orderDao.getOrdersByUser(user);
        Map<Long, OrderDTO> orderMap = orders.stream().collect(Collectors.toMap(Order::getId,
                order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderMapper.orderToDTO(order, orderDTO);
                    return orderDTO;
                }));
        return orderMap;
    }


    public void deleteOrders(Long id, DefaultUser user) {
        if(user.getRole().equals(Role.ADMIN)){
            orderDao.deleteOrder(id);
        } else if (user.getRole().equals(Role.USER)){
            if (orderDao.isOrderExistsByIdAndUser(id, (SimpleUser)user)) {
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
        System.out.println("========================" + order + "==================================");
        orderMapper.orderToDTO(order, orderDTO);
        System.out.println("========================" + orderDTO + "==================================");
    }

    public Map<Long, OrderDTO> getOrders(Specification<Order> spec) {
        List<Order> orders = orderDao.getOrders(spec);
        Map<Long, OrderDTO> orderMap = orders.stream().collect(Collectors.toMap(Order::getId,
                order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderMapper.orderToDTO(order, orderDTO);
                    return orderDTO;
                }));
        return orderMap;
    }
}
