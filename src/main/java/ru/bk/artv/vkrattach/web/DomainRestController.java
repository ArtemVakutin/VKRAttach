package ru.bk.artv.vkrattach.web;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.config.security.serializers.SessionsRestarter;
import ru.bk.artv.vkrattach.services.ConfigDataCrudService;
import ru.bk.artv.vkrattach.services.model.ConfigData;
import ru.bk.artv.vkrattach.services.ConfigDataService;
import ru.bk.artv.vkrattach.web.dto.ConfigDataDto;

import java.util.List;
import java.util.Map;

/**
 * REST-контроллир для получения основных конфигурационных данных при старте клиентского приложения
 */
@Slf4j
@RestController
@RequestMapping(path = "rest/domain")
public class DomainRestController {

    //сервис, предоставляющий domain-data
    private final ConfigDataService configDataService;

    public DomainRestController(ConfigDataService allDomainData, ConfigDataCrudService configDataCrudService, ConfigurableApplicationContext context, SessionsRestarter sessionsRestarter) {
        this.configDataService = allDomainData;
    }

    /**
     * Получение config-data
     *
     * @return Map типа "вид данных" - "список возможных данных"
     */
    @GetMapping
    public Map<ConfigData.ConfigType, List<ConfigData>> getDomainData() {
        return configDataService.getDomainData();
    }

}
