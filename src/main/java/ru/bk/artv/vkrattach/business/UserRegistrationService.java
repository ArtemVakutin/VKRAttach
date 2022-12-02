package ru.bk.artv.vkrattach.business;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.internal.util.io.CharSequenceReader;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.UserRegistrationDao;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.User;
import ru.bk.artv.vkrattach.domain.dto.UserDTO;
import ru.bk.artv.vkrattach.domain.dto.UserRegistrationDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotPatchedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.exceptions.UserNotAuthorizedException;

@Slf4j
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
    public void registerNewUser(UserRegistrationDTO request) {
        User userReq = webToUser(request);

        User user = (User) Hibernate.unproxy(userRegistrationDao.checkUser(userReq.getEmail()));
        if (user == null) {
            userRegistrationDao.saveUser(userReq);
        } else {
            throw new ResourceNotSavedException("User : " + user.getEmail() + "is already exist");
        }
    }

    //Мэппит DTO в модельку, устанавливает Role.USER и шифрует пароль
    private User webToUser(UserRegistrationDTO request) {
        User user = userMapper.toUser(request);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    public UserDTO getUser(User user) {
        if (user != null) {
            return userMapper.toUserDTO(user);
        }
        throw new UserNotAuthorizedException("User not authorizated, please get authorization");
    }

    public void patchUser(UserRegistrationDTO userDTO, User user) {
        log.info(userDTO.getOldPassword());
        log.info("Пароли :: " + passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword()));
        log.info(user.getPassword());
        if (userDTO.getPassword() != null) {
            if (passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword())) {
                changeUserFields(userDTO, user);
            } else {
                throw new ResourceNotPatchedException("password is incorrect");
            }
        } else {
            changeUserFields(userDTO, user);
        }
        userRegistrationDao.saveUser(user);
    }


    private void changeUserFields(UserRegistrationDTO userDTO, User user) {
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getFaculty() != null) {
            user.setFaculty(userDTO.getFaculty());
        }
        if (userDTO.getGroup() != null) {
            user.setGroup(userDTO.getGroup());
        }
        if (userDTO.getName() != null) {
            user.setName(userDTO.getGroup());
        }
        if (userDTO.getSurname() != null) {
            user.setSurname(userDTO.getSurname());
        }
        if (userDTO.getPatronymic() != null) {
            user.setPatronymic(userDTO.getPatronymic());
        }
        if (userDTO.getTelephone() != null) {
            user.setTelephone(userDTO.getTelephone());
        }
        if (userDTO.getYearOfRecruitment() != null) {
            user.setYearOfRecruitment(userDTO.getYearOfRecruitment());
        }
        if (userDTO.getPassword() != null && userDTO.getOldPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
    }
}
