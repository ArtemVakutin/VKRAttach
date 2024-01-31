package ru.bk.artv.vkrattach.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.bk.artv.vkrattach.dao.repository.DomainDataRepository;
import ru.bk.artv.vkrattach.services.ConfigDataService;
import ru.bk.artv.vkrattach.services.model.ConfigData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Создает конфиг для отправки на клиент после старта основного приложения.
 */
@Slf4j
@Component
@AllArgsConstructor
public class ConfigDataPropertiesLoader {

    ConfigDataService data;
    DomainDataRepository dataRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void buildDomainData() {

        List<ConfigData> all = dataRepository.findAll();

        ConfigData.ConfigType[] values = ConfigData.ConfigType.values();
        Map<ConfigData.ConfigType, List<ConfigData>> dataMap = data.getDomainData();
        dataMap.clear();

        Arrays.stream(values).forEach(type -> dataMap.put(type, all.stream()
                .filter(data -> data.getType().equals(type))
                .collect(Collectors.toList())));

        dataMap.keySet().forEach(type -> {
            if (dataMap.get(type).isEmpty()) {
                log.error("ConfigData with type {} is not allowed", type.name());
            }
        });

        log.info("Data configuration complete successful");
    }
}
