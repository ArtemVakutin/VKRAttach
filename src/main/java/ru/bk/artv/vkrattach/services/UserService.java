package ru.bk.artv.vkrattach.services;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.bk.artv.vkrattach.services.mappers.UserMapper;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.domain.user.Role;
import ru.bk.artv.vkrattach.dto.UserToClientDTO;
import ru.bk.artv.vkrattach.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotPatchedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.exceptions.UserNotAuthorizedException;
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
        public Long registerNewUser(UserToPatchDTO request) {
        log.info(request.toString());

        if (request.getId() != null){
            throw new ResourceNotSavedException("User : " + request.getLogin() + " id is not NULL");
        }

        if(userDao.checkLoginExisted(request.getLogin())){
            throw new ResourceNotSavedException("User : " + request.getLogin() + " is already exist");
        }
        DefaultUser defaultUser = webToUser(request);
        return userDao.saveUser(defaultUser);
    }

    //Мэппит DTO в модельку и шифрует пароль
    private DefaultUser webToUser(UserToPatchDTO request) {
        DefaultUser user = userMapper.toDefaultUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }

    public UserToClientDTO getUser(DefaultUser user) {
        if (user != null) {
            return userMapper.toUserDTO(user);
        }
        throw new UserNotAuthorizedException("User not authorizated, please get authorization");
    }

    public UserToClientDTO getUser(Long id) {

        DefaultUser user = userDao.findUserById(id);
        if (user instanceof SimpleUser) {
            return userMapper.toUserDTO((SimpleUser)user);
        } else {
            throw new AccessDeniedException("Access to data denied");
        }
    }

    //Methods patchSimpleUserWithPassword & patchSimpleUser are differents only by validation steps.
    //In patchSimple no validation in field password. Default.class is password validation
    @Validated({UserToPatchDTO.ValidationStepForPatch.class, Default.class})
    public void patchSimpleUserWithPassword(@Valid UserToPatchDTO userDTO, DefaultUser user) {
    }

    @Validated({UserToPatchDTO.ValidationStepForPatch.class})
    public void patchSimpleUser(@Valid UserToPatchDTO userDTO, DefaultUser user) {
        patchUser(userDTO, user);
    }
    
    private void patchUser(UserToPatchDTO userDTO, DefaultUser user){
        DefaultUser userToPatch;

        if (user.getRole() == Role.ADMIN) {
            userToPatch = userDao.findUserById(userDTO.getId());
            userMapper.toDefaultUser(userDTO, userToPatch);
            if (userDTO.getPassword() != null && userDTO.getPassword() != "") {
                userToPatch.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }
            userDao.saveUser(userToPatch);
        }

        if (user.getRole() == Role.USER) {
            userMapper.toDefaultUser(userDTO, user);
            if (userDTO.getPassword() != null && userDTO.getPassword() != "") {
                if (passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                } else {
                    throw new ResourceNotPatchedException("password is incorrect");
                }
            }
            userDao.saveUser(user);
        }
    }




    public List<UserToClientDTO> findUsers(Specification<SimpleUser> specs) {
        List<SimpleUser> users = userDao.findSimpleUsers(specs);
        return users.stream().map(user -> userMapper.toUserDTO(user)).collect(Collectors.toList());
    }

    public List<UserToClientDTO> findUsers(Role role) {
        List<DefaultUser> users = userDao.findDefaultUsers(role);
        return users.stream().map(user -> userMapper.toUserDTO(user)).collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }
}
