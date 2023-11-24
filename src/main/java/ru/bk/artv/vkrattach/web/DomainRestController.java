package ru.bk.artv.vkrattach.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.domain.ConfigData;
import ru.bk.artv.vkrattach.services.ConfigDataService;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "rest/domain")
public class DomainRestController {

ConfigDataService configDataService;

    public DomainRestController(ConfigDataService allDomainData) {
        this.configDataService = allDomainData;
    }

//    @GetMapping
//    public AllConfigDataDto getDepartments() {
//        return allConfigData;
//    }

    @GetMapping
    public Map<ConfigData.ConfigType, List<ConfigData>> getDomainData(HttpServletResponse response) {
        return configDataService.getDomainData();
    }
}
