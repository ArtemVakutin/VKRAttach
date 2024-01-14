package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;


@Repository
public interface SimpleUserRepository extends JpaRepository<SimpleUser, Long>, JpaSpecificationExecutor<SimpleUser> {

}
