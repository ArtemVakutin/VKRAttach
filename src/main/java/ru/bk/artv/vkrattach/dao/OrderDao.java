package ru.bk.artv.vkrattach.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.repository.LecturerRepository;
import ru.bk.artv.vkrattach.dao.repository.OrderRepository;
import ru.bk.artv.vkrattach.dao.repository.ThemeRepository;
import ru.bk.artv.vkrattach.domain.*;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderDao {

    OrderRepository orderRepository;

    public OrderDao(OrderRepository orderRepository, LecturerRepository lecturerRepository, ThemeRepository themeRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrdersByUser(SimpleUser user) {
        List<Order> orders = orderRepository.findByUser(user);
        if (!orders.isEmpty()) {
            return orders;
        }
        throw new ResourceNotFoundException("Orders by user : " + user.getId() + " are not found");
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public void addOrder(Order order) {
        orderRepository.saveAndFlush(order);
     }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public boolean isOrderExistsByIdAndUser(Long id, SimpleUser user) {
        return orderRepository.existsByIdAndUser(id, user);
    }

    public boolean isOrderExistsByTheme(Theme theme) {
        return orderRepository.existsByTheme(theme);
    }



    public List<Order> getOrders(Specification<Order> spec) {
        List<Order> orders = orderRepository.findAll(spec);
        if (orders.isEmpty()){
            throw new ResourceNotFoundException(" заявок с такими данными не найдено");
        }
        return orders;
    }
}
