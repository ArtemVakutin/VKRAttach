package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.user.Role;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;

import java.util.List;


@Repository
public interface DefaultUserRepository extends JpaRepository<DefaultUser, Long> {

    DefaultUser findByLoginIgnoreCase(String login);
    List<DefaultUser> findByRole(Role role);
    boolean existsByLogin(String login);

}
