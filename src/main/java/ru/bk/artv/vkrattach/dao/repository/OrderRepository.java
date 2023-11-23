package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByUser(SimpleUser user);

    List<Order> findByLecturer(Lecturer lecturer);

    boolean existsByIdAndUser(Long id, SimpleUser user);

    @Query("select ord from Order ord where ord.theme.department = :department and" +
            " ord.user.faculty = :faculty" +
            " and ord.user.year = :year")
    List<Order> findOrdersForModerator(@Param("department") String department,
                                       @Param("faculty") String faculty,
                                       @Param("year") String year);

    void deleteByUser(SimpleUser id);

    boolean existsByTheme(Theme theme);
}
