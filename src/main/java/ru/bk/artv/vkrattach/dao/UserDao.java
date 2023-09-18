package ru.bk.artv.vkrattach.dao;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.DefaultUserRepository;
import ru.bk.artv.vkrattach.dao.repository.OrderRepository;
import ru.bk.artv.vkrattach.dao.repository.SimpleUserRepository;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDao {

    DefaultUserRepository defaultUserRepository;
    SimpleUserRepository simpleUserRepository;
    OrderRepository orderRepository;

    public boolean checkLoginExisted(String str){
        return defaultUserRepository.existsByLogin(str);
    }

    public DefaultUser findUserById(Long id){
       return defaultUserRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id : " + id + " is not found "));
    }

    public Long saveUser(DefaultUser user){
        return defaultUserRepository.saveAndFlush(user).getId();
    }

    public List<SimpleUser> findSimpleUsers(Specification<SimpleUser> specs) {
        List<SimpleUser> users = simpleUserRepository.findAll(specs);
        if (!users.isEmpty()) {
            return users;
        }
        throw new ResourceNotFoundException("Пользователи со спецификациями не найдены");
    }

    public List<DefaultUser> findDefaultUsers(Role role) {
        List<DefaultUser> users = defaultUserRepository.findByRole(role);
        if (!users.isEmpty()) {
            return users;
        }
        throw new ResourceNotFoundException("Пользователи с ролью " + role.name() + " не найдены");
    }

    public void deleteUser(Long userId) {
        DefaultUser user = defaultUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id : " + userId + "не существует"));
        if(user instanceof SimpleUser){
            orderRepository.deleteByUser((SimpleUser)user);
        }
        defaultUserRepository.deleteById(userId);
    }
}