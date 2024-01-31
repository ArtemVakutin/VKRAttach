package ru.bk.artv.vkrattach.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.web.dto.UserDto;
import ru.bk.artv.vkrattach.exceptions.ResourceNotPatchedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.exceptions.UserNotAuthorizedException;
import ru.bk.artv.vkrattach.services.mappers.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Класс, содержащий методы для получения, обраобтки и изменения информации о пользователях.
 * Содержит как CRUD методы, так и методы для запроса информации о пользователях по фильтрам
 */

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

/**
 * Регистрирует нового пользователя. Проверяет, существует ли пользователь в базе
 * @param request - пользователь DTO без айдишника
 */
    @Transactional
     public UserDto registerNewUser(UserDto request) {
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

    //Мэппит DTO в модельку с помощью мэппера и шифрует пароль
    private DefaultUser webToUser(UserDto request) {
        DefaultUser user = userMapper.toDefaultUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return user;
    }

    /**
     * Мэппит DefaultUser в UserDto. Метод нужен для запроса пользователя при аутентификации
     * @param user авторизированный пользователь. Если Null выбрасывает ошибку
     * @return пользователя в Dto.
     */
    public UserDto getUser(DefaultUser user) {
        if (user != null) {
            return userMapper.toUserDTO(user);
        }
        throw new UserNotAuthorizedException("User not authorizated, please get authorization");
    }

    /** Возвращает пользователя по айдишнику
     * @param id - айдишник пользователя
     * @return SimpleUser в Dto
     * @throws AccessDeniedException если попытались запросить модератора/админа
     */
    public UserDto getUser(Long id) {

        DefaultUser user = userDao.findUserById(id);
        if (user instanceof SimpleUser) {
            return userMapper.toUserDTO(user);
        } else {
            throw new AccessDeniedException("Access to data denied");
        }
    }

    /** Для изменения данных пользователя админом. Может патчиться любой пользователь.
     * @param userDTO пользователь, который патчится. Сохраняет все поля.
     * @return пропатченный пользователь
     */
    public UserDto patchUser(UserDto userDTO) {
        DefaultUser user = userDao.findUserById(userDTO.getId());

        userMapper.toDefaultUser(userDTO, user);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userDao.saveUser(user);
        return userMapper.toUserDTO(user);
    }

    /** Для изменения данных пользователя самим пользователем. Сверяется айдишник Dto и авторизированного
     * @param userDTO пользователь, который патчится. Сохраняет все поля.
     * @param user авторизированный пользователь
     * @return пропатченный пользователь
     */
    public UserDto patchUser(UserDto userDTO, DefaultUser user) {
        if (!Objects.equals(user.getId(), userDTO.getId())) {
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

    /** Для запроса пользователей по фильтрам. ТОЛЬКО для SimpleUser (иначе со Specification куча проблем)
     * @param specs набор спецификаций.
     * @return список Dto пользователей
     */
    public List<UserDto> findUsers(Specification<SimpleUser> specs) {
        List<SimpleUser> users = userDao.findSimpleUsers(specs);
        return users.stream().map(user -> userMapper.toUserDTO(user)).collect(Collectors.toList());
    }

    /** Для запроса пользователей по роли. По факту для запроса модераторов и админов.
     * @param role права доступа
     * @return список Dto пользователей
     */
    public List<UserDto> findUsers(Role role) {
        List<DefaultUser> users = userDao.findDefaultUsers(role);
        return users.stream().map(user -> userMapper.toUserDTO(user)).collect(Collectors.toList());
    }

    /** Удаляет пользователя с концами.
     * @param userId айдишник
     */
    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }

    /** Изменение пароля пользователя.
     * @param user Пользователь из базы данных
     * @param userDto данные нового и старого пароля, пришедшие от клиента. Остальные поля null. Новый пароль валидируется
     *                на web уровне.
     */
    public void changeUserPassword(UserDto userDto, DefaultUser user) {
        if (!passwordEncoder.matches(userDto.getOldPassword(), user.getPassword())) {
            throw new ResourceNotSavedException("Старый пароль не совпадает с паролем аккаунта");
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDao.saveUser(user);
    }
}
