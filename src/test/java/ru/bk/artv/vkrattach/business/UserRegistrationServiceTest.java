package ru.bk.artv.vkrattach.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.User;
import ru.bk.artv.vkrattach.domain.dto.UserRegistrationDTO;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRegistrationServiceTest {



    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRegistrationService userService;
    static UserRegistrationDTO userDTO;

    @BeforeAll
    static void beforeAll() {
        userDTO = new UserRegistrationDTO();
        userDTO.setEmail("artv@bkff.ru");
        userDTO.setFaculty("НБФЗОП");
        userDTO.setGroup("NBFZOP-151");
        userDTO.setName("Ivan");
        userDTO.setSurname("Ivanov");
        userDTO.setPatronymic("Ivanovich");
        userDTO.setPassword("11111");
        userDTO.setTelephone("90159141214");
    }

    @Test
    void webToUser(){
        User user = userMapper.toUser(userDTO);
        assertEquals(user.getName(), "Ivan");
        assertEquals(user.getRole(), Role.USER);
        System.out.println(user);
    }

    @Test
    void registerNewUser() {
        userService.registerNewUser(userDTO);
    }

}