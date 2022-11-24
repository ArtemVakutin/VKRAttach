package ru.bk.artv.vkrattach.dao;

import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.UserRepository;
import ru.bk.artv.vkrattach.domain.User;

@Service
public class UserRegistrationDao {

    UserRepository userRepository;

    public UserRegistrationDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User checkUser(String str){
        User byEmail = userRepository.findByEmail(str);
        return byEmail;
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

}
