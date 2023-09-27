package ru.bk.artv.vkrattach.services;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.domain.ConfigData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Service
public class ConfigDataService {

    private final Map<ConfigData.ConfigType, List<ConfigData>> domainData = new HashMap<>();

    public String getDataLabel(ConfigData.ConfigType type, String data) {
        List<ConfigData> configData = domainData.get(type);
        List<ConfigData> collect = configData.stream().filter(config -> config.getValue().equals(data)).collect(Collectors.toList());

        if (collect.isEmpty()) {
            return data;
        }
        if (collect.get(0).getLabel().equals("отсутствует")){
            return "";
        }
        return collect.get(0).getLabel();
    }
}
