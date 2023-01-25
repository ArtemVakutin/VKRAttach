package ru.bk.artv.vkrattach.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.bk.artv.vkrattach.business.mappers.UserMapper;
import ru.bk.artv.vkrattach.dao.UserRegistrationDao;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.dto.UserToClientDTO;
import ru.bk.artv.vkrattach.domain.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotPatchedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.exceptions.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void registerNewUser(UserToPatchDTO request) {
        if (request.getId() != null || request.getId() == 0){
            throw new ResourceNotSavedException("User : " + request.getLogin() + " id is not NULL");
        }
        if(userRegistrationDao.checkUser(request.getLogin())){
            throw new ResourceNotSavedException("User : " + request.getLogin() + "is already exist");
        }
        DefaultUser defaultUser = webToUser(request);
        userRegistrationDao.saveUser(defaultUser);
    }

    //Мэппит DTO в модельку и шифрует пароль
    private DefaultUser webToUser(UserToPatchDTO request) {
        DefaultUser user = userMapper.toDefaultUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    public UserToClientDTO getUser(DefaultUser user) {
        if (user != null) {
            return userMapper.toUserDTO(user);
        }
        throw new UserNotAuthorizedException("User not authorizated, please get authorization");
    }

    public void patchUser(UserToPatchDTO userDTO, DefaultUser user) {
        SimpleUser userToPatch;

        if (user.getRole() == Role.ADMIN) {
           userToPatch = (SimpleUser) userRegistrationDao.findUserById(userDTO.getId());
        } else {
            userToPatch = (SimpleUser) user;
        }

        if (userDTO.getPassword() != null) {
            if (passwordEncoder.matches(userDTO.getOldPassword(), user.getPassword())) {
                changeUserFields(userDTO, userToPatch);
            } else {
                throw new ResourceNotPatchedException("password is incorrect");
            }
        } else {
            changeUserFields(userDTO, userToPatch);
        }
        userRegistrationDao.saveUser(user);
    }

    private void changeUserFields(UserToPatchDTO userDTO, SimpleUser user) {

        if (userDTO.getLogin() != null) {
            user.setLogin(userDTO.getLogin());
        }
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

    public Map<Long, UserToClientDTO> findUsers(Specification<SimpleUser> specs) {
        List<SimpleUser> users = userRegistrationDao.findUsers(specs);
        return users.stream().collect(Collectors.toMap(DefaultUser::getId, user -> userMapper.toUserDTO(user)));
    }
}
