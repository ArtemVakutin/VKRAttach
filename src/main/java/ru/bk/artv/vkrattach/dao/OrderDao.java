package ru.bk.artv.vkrattach.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.OrderRepository;
import ru.bk.artv.vkrattach.services.model.Lecturer;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Класс создан как промежуточное между сервисами и репозиториями Spring JPA.
 * В большинстве случаев просто перенаправляет в репозиторий.
 * В ряде случаев при отсутствии в списках заявок выбрасывает ResourceNotFountExceptions
 */
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

    public Optional<Order> getOrder(Long id, SimpleUser user) {
        return orderRepository.findByIdAndUser(id, user);
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



    public List<Order> getOrders(Lecturer lecturer) {
        return orderRepository.findByLecturer(lecturer);
    }


    public List<Order> getOrders(Specification<Order> spec) {
        List<Order> orders = orderRepository.findAll(spec);
        if (orders.isEmpty()){
            throw new ResourceNotFoundException("Заявок с такими данными не найдено");
        }
        return orders;
    }
}
