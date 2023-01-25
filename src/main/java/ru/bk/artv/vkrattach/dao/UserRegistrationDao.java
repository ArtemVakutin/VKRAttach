package ru.bk.artv.vkrattach.dao;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.DefaultUserRepository;
import ru.bk.artv.vkrattach.dao.repository.SimpleUserRepository;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRegistrationDao {

    DefaultUserRepository userRepository;
    SimpleUserRepository simpleUserRepository;



    public boolean checkUser(String str){
        return userRepository.existsByLogin(str);
    }

    public DefaultUser findUserById(Long id){
       return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id : " + id + " is not found "));
    }

    public void saveUser(DefaultUser user){
        userRepository.save(user);
        userRepository.flush();
    }

    public List<SimpleUser> findUsers(Specification<SimpleUser> specs) {
        List<SimpleUser> users = simpleUserRepository.findAll(specs);
        if (!users.isEmpty()) {
            return users;
        }
        throw new ResourceNotFoundException("Пользователи со спецификациями не найдены");
    }
}
