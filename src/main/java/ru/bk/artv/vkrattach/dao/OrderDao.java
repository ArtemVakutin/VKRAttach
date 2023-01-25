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
    LecturerRepository lecturerRepository;

    public OrderDao(OrderRepository orderRepository, LecturerRepository lecturerRepository, ThemeRepository themeRepository) {
        this.orderRepository = orderRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @Transactional
    public List<Order> getOrdersByUser(SimpleUser user) {
        List<Order> byUser = orderRepository.findByUser(user);
        if (byUser.size() > 0) {
            return byUser;
        }
        throw new ResourceNotFoundException("Orders for User : "
                + user.getId()
                + " "
                + user.getLogin()
                + " "
                + user.getSurname()
                + " "
                + user.getName()
                + "are not found");
    }

    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Lecturer> getLecturerById(Long id) {
        return lecturerRepository.findById(id);
    }

    public void addOrder(Order order) {
        orderRepository.save(order);
        orderRepository.flush();
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public boolean isOrderExistsByIdAndUser(Long id, SimpleUser user) {
        return orderRepository.existsByIdAndUser(id, user);
    }

    public List<Order> getOrders(Specification<Order> spec) {
        return orderRepository.findAll(spec);
    }
}
