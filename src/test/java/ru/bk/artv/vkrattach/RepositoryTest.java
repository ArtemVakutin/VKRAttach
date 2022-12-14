package ru.bk.artv.vkrattach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.repository.LecturerRepository;
import ru.bk.artv.vkrattach.dao.repository.OrderRepository;
import ru.bk.artv.vkrattach.dao.repository.UserRepository;
import ru.bk.artv.vkrattach.dao.repository.ThemeRepository;
import ru.bk.artv.vkrattach.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class RepositoryTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    OrderRepository requestRepository;

    @Autowired
    LecturerRepository lecturerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ThemeRepository themeRepository;

    @Test
    void SaveRepTest(){

        Lecturer lecturer = new Lecturer();
        lecturer.setName("Лектор");
        lecturer.setSurname("Лекторов");
        lecturer.setPatronymic("Лекторович");
        lecturer.setEmail("lector@lector.ru");
        lecturer.setDepartment(Department.UPR);

        lecturerRepository.save(lecturer);
        lecturerRepository.flush();

        User user = new User();
        user.setName("Pavel");
        user.setSurname("Pavlov");
        user.setPatronymic("Pavlovich");
        user.setFaculty("НБФЗОП");
        user.setGroup("321");
        user.setRegistrationDate(LocalDateTime.now());
        user.setTelephone("9049503923");
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode("11111"));
        user.setEmail("user@user.com");
        user.setYearOfRecruitment("2008");

        userRepository.save(user);
        userRepository.flush();

        Theme theme = new Theme();
        theme.setThemeName("Разгильдяйство и распиздяйство в обществе дураков");
        theme.setFaculty("НБФЗОП");
        theme.setThemeDepartment(Department.UPR);

        themeRepository.save(theme);
        themeRepository.flush();

        Order order = new Order();
        order.setUser(user);
        order.setLecturer(lecturer);
        order.setOrderStatus(Order.OrderStatus.UNDER_CONSIDERATION);
        order.setTheme(theme);
        order.setRequestDate(LocalDate.now());
        order.setRequestStatusDate(LocalDate.now());

        requestRepository.save(order);
        requestRepository.flush();
    }

    @Test
    @Transactional
    void ReadTest(){
        List<Order> all = requestRepository.findAll();
        all.stream().forEach(order -> System.out.println(order));
    }




}
