package ru.bk.artv.vkrattach.services;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.Role;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.dto.UserDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotPatchedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.exceptions.UserNotAuthorizedException;
import ru.bk.artv.vkrattach.services.mappers.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    UserMapper userMapper;
    UserDao userDao;
    PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO registerNewUser(UserDTO request) {
        log.info(request.toString());

        if (request.getId() != null) {
            throw new ResourceNotSavedException("User : " + request.getLogin() + " id is not NULL");
        }

        if (userDao.checkLoginExisted(request.getLogin())) {
            throw new ResourceNotSavedException("User : " + request.getLogin() + " is already exist");
        }
        DefaultUser defaultUser = webToUser(request);
        userDao.saveUser(defaultUser);
        return userMapper.toUserDTO(defaultUser);
    }

    //Мэппит DTO в модельку и шифрует пароль
    private DefaultUser webToUser(UserDTO request) {
        DefaultUser user = userMapper.toDefaultUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }

    public UserDTO getUser(DefaultUser user) {
        if (user != null) {
            return userMapper.toUserDTO(user);
        }
        throw new UserNotAuthorizedException("User not authorizated, please get authorization");
    }

    public UserDTO getUser(Long id) {

        DefaultUser user = userDao.findUserById(id);
        if (user instanceof SimpleUser) {
            return userMapper.toUserDTO((SimpleUser) user);
        } else {
            throw new AccessDeniedException("Access to data denied");
        }
    }
    //Только для админов, может пропатчиться любой юзер
    public UserDTO patchUser(UserDTO userDTO) {
        DefaultUser user = userDao.findUserById(userDTO.getId());
        userMapper.toDefaultUser(userDTO, user);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userDao.saveUser(user);
        return userMapper.toUserDTO(user);
    }

    //Для обычных пользователей, проверка на совпадение айди.
    public UserDTO patchUser(UserDTO userDTO, DefaultUser user) {
        if (user.getId() != userDTO.getId()) {
            throw new AccessDeniedException("User ID is not authenticated user ID");
        }

        userMapper.toDefaultUser(userDTO, user);

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            if (passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            } else {
                throw new ResourceNotPatchedException("password is incorrect");
            }
        }
        userDao.saveUser(user);

        return userMapper.toUserDTO(user);
    }


    public List<UserDTO> findUsers(Specification<SimpleUser> specs) {
        List<SimpleUser> users = userDao.findSimpleUsers(specs);
        return users.stream().map(user -> userMapper.toUserDTO(user)).collect(Collectors.toList());
    }

    public List<UserDTO> findUsers(Role role) {
        List<DefaultUser> users = userDao.findDefaultUsers(role);
        return users.stream().map(user -> userMapper.toUserDTO(user)).collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }


}
