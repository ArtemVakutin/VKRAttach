package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bk.artv.vkrattach.services.model.ConfigData;

public interface DomainDataRepository extends JpaRepository<ConfigData, Long> {
}
