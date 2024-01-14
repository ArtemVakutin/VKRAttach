package ru.bk.artv.vkrattach.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.services.model.ConfigData;
import ru.bk.artv.vkrattach.services.ConfigDataService;

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

    public DomainRestController(ConfigDataService allDomainData) {
        this.configDataService = allDomainData;
    }

//    @GetMapping
//    public AllConfigDataDto getDepartments() {
//        return allConfigData;
//    }

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
