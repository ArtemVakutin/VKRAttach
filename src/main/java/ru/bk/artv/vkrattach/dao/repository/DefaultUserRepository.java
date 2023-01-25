package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;


@Repository
public interface UserRepository extends JpaRepository<DefaultUser, Long>, JpaSpecificationExecutor<SimpleUser> {

    DefaultUser findByLogin(String login);
    boolean existsByLogin(String login);

}
