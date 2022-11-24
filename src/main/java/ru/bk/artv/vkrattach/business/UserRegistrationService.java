package ru.bk.artv.vkrattach.business;

import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.UserRegistrationDao;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.User;
import ru.bk.artv.vkrattach.domain.dto.UserDTO;
import ru.bk.artv.vkrattach.domain.dto.UserRegistrationDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.exceptions.UserNotAuthorizedException;

@Service
public class UserRegistrationService {

    UserMapper userMapper;
    UserRegistrationDao userRegistrationDao;
    PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserMapper userMapper, UserRegistrationDao userRegistrationDao, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRegistrationDao = userRegistrationDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerNewUser(UserRegistrationDTO request){
        User userReq = webToUser(request);

        User user = (User)Hibernate.unproxy(userRegistrationDao.checkUser(userReq.getEmail()));
        if (user == null) {
            userRegistrationDao.saveUser(userReq);
        } else {
            throw new ResourceNotSavedException("User : " + user.getEmail() + "is already exist");
        }
    }
    //Мэппит DTO в модельку, устанавливает Role.USER и шифрует пароль
    private User webToUser(UserRegistrationDTO request){
        User user = userMapper.toUser(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    public UserDTO getUser(User user){
        if (user !=null){
        return userMapper.toUserDTO(user);
        }
        throw new UserNotAuthorizedException("User not authorizated, please get authorization");
    }

    }
