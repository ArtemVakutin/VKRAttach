package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}
