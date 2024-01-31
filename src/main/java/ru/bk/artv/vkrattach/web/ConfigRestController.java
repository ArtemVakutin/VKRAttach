package ru.bk.artv.vkrattach.web;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.config.security.serializers.SessionsRestarter;
import ru.bk.artv.vkrattach.services.ConfigDataCrudService;
import ru.bk.artv.vkrattach.services.model.ConfigData;
import ru.bk.artv.vkrattach.web.dto.ConfigDataDto;

import java.util.List;

@RestController
@RequestMapping(path = "rest/config")
public class ConfigRestController {
    private final ConfigDataCrudService configDataCrudService;
    private final SessionsRestarter sessionsRestarter;

    public ConfigRestController(ConfigDataCrudService configDataCrudService, SessionsRestarter sessionsRestarter) {
        this.configDataCrudService = configDataCrudService;
        this.sessionsRestarter = sessionsRestarter;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public List<ConfigData> getDomainDataByAdmin(@RequestParam ConfigData.ConfigType type) {
        return configDataCrudService.getDataByDataType(type);
    }

    @PostMapping(path = "/restart")
    @Secured("ROLE_ADMIN")
    public void restartServer() throws Exception {
        sessionsRestarter.closeAllSessions();
    }

    @PutMapping
        @Secured("ROLE_ADMIN")
    public ConfigDataDto putConfigData(@RequestBody @Validated({Default.class}) ConfigDataDto data) {
        return configDataCrudService.putData(data);
    }

    @PatchMapping

    @Secured("ROLE_ADMIN")
    public ConfigDataDto patchConfigData(@RequestBody  @Validated({Default.class, ConfigDataDto.ValidationId.class}) ConfigDataDto data) {
        return configDataCrudService.patchData(data);
    }

    @DeleteMapping
    @Secured("ROLE_ADMIN")
    public void deleteConfigData(@RequestParam Long id) {
        configDataCrudService.deleteData(id);
    }
}
