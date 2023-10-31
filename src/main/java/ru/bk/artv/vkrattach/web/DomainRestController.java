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

        Cookie cookie = new Cookie("token", "111111111111111111111111");
        cookie.setPath("/");//проверить
//        cookie.setDomain("localhost");
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setMaxAge((int) ChronoUnit.SECONDS.between(Instant.now(), Instant.now().plus(Duration.ofDays(1))));

        response.addCookie(cookie);
        return configDataService.getDomainData();
    }
}
