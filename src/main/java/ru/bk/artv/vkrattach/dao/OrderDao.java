package ru.bk.artv.vkrattach.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.repository.LecturerRepository;
import ru.bk.artv.vkrattach.dao.repository.OrderRepository;
import ru.bk.artv.vkrattach.dao.repository.ThemeRepository;
import ru.bk.artv.vkrattach.domain.*;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderDao {

    OrderRepository orderRepository;
    LecturerRepository lecturerRepository;
    ThemeRepository themeRepository;

    public OrderDao(OrderRepository orderRepository, LecturerRepository lecturerRepository, ThemeRepository themeRepository) {
        this.orderRepository = orderRepository;
        this.lecturerRepository = lecturerRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public List<Order> getOrdersByUser(User user) {
        List<Order> byUser = orderRepository.findByUser(user);
        if (byUser.size() > 0) {
            return byUser;
        }
        throw new ResourceNotFoundException("Orders for User : "
                + user.getId()
                + " "
                + user.getEmail()
                + " "
                + user.getSurname()
                + " "
                + user.getName()
                + "are not found");
    }

    public List<Lecturer> getLecturersByDepartment(String department) {
        try {
            List<Lecturer> lecturerByDepartment = lecturerRepository.getLecturerByDepartment(Department.valueOf(department));
            if (lecturerByDepartment.size() > 0) {
                return lecturerByDepartment;
            }
            throw new ResourceNotFoundException("Lecturers from Department : " + department + " are not found");
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No enum constant : " +
                    department + " : in ru.bk.artv.vkrattach.domain.Department");
        }
    }

    public List<Theme> getThemesByDepartmentFacultyYear(String department, String faculty, String year) {
        try {
            List<Theme> themeList = themeRepository.getByThemeDepartmentAndFacultyAndYearOfRecruitment(Department.valueOf(department), faculty, year);
            if (themeList.size() > 0) {
                return themeList;
            }
            throw new ResourceNotFoundException("Themes from Department : " + department +
                    "and Faculty : " + faculty + " and Year :" + year
                    + " are not found");
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No enum constant : " +
                    department + " : in ru.bk.artv.vkrattach.domain.Department");
        }
    }

    public Optional<Theme> getThemeById(Long id) {
        return themeRepository.findById(id);
    }

    public Optional<Lecturer> getLecturerById(Long id) {
        return lecturerRepository.findById(id);
    }

    public void addOrder(Order order) {
        orderRepository.save(order);
        orderRepository.flush();
    }

    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }

    public boolean isOrderExistsByIdAndUser(Long id, User user){
        return orderRepository.existsByIdAndUser(id, user);
    }

}
