package ru.bk.artv.vkrattach.web;

import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.domain.ConfigData;
import ru.bk.artv.vkrattach.services.ConfigDataService;

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
    public Map<ConfigData.ConfigType, List<ConfigData>> getDomainData() {
        return configDataService.getDomainData();
    }
}
