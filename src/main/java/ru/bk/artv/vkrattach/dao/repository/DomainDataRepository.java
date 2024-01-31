package ru.bk.artv.vkrattach.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bk.artv.vkrattach.services.model.ConfigData;

import java.util.List;

public interface DomainDataRepository extends JpaRepository<ConfigData, Long> {

    List<ConfigData> getByType (ConfigData.ConfigType type);
    boolean existsByTypeAndValue (ConfigData.ConfigType type, String value);

}
