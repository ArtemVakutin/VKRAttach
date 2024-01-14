package ru.bk.artv.vkrattach.dao.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test-h2")
class DefaultUserRepositoryTest {

    @Autowired
    DefaultUserRepository repository;

    @Test
    void findByLoginIgnoreCase() {
        DefaultUser user1 = repository.findByLoginIgnoreCase("User1");
        assertNotNull(user1);
    }

    @Test
    void findByRole() {
        List<DefaultUser> byRole = repository.findByRole(Role.USER);
        assertFalse(byRole.isEmpty());
    }

    @Test
    void existsByLogin() {
        boolean existsByLogin = repository.existsByLogin("ADMIN");
        assertTrue(existsByLogin);
    }
}