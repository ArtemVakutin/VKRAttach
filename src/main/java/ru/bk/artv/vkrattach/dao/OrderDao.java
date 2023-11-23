package ru.bk.artv.vkrattach.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.OrderRepository;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderDao {

    OrderRepository orderRepository;

    public OrderDao(OrderRepository orderRepository) {
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


    public boolean isOrderExists(Theme theme) {
        return orderRepository.existsByTheme(theme);
    }

    public boolean isOrderExists(Long id, SimpleUser user) {
        return orderRepository.existsByIdAndUser(id, user);
    }

    public List<Order> getOrders(Lecturer lecturer) {
        return orderRepository.findByLecturer(lecturer);
    }


    public List<Order> getOrders(Specification<Order> spec) {
        List<Order> orders = orderRepository.findAll(spec);
        if (orders.isEmpty()){
            throw new ResourceNotFoundException(" заявок с такими данными не найдено");
        }
        return orders;
    }
}
