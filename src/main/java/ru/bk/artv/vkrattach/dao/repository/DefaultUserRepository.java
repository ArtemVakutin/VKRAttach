package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

import java.util.List;


@Repository
public interface DefaultUserRepository extends JpaRepository<DefaultUser, Long> {

    DefaultUser findByLogin(String login);
    List<DefaultUser> findByRole(Role role);
    boolean existsByLogin(String login);

}
