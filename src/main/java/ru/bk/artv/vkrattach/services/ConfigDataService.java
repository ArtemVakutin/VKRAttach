package ru.bk.artv.vkrattach.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.repository.DomainDataRepository;
import ru.bk.artv.vkrattach.services.model.ConfigData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Необходим для упрощения генерации Docx-файлов, а также получения domainData клиентами
 * Так как конфиг содержит Value и label в некоторых случаях необходимо вместо например "НБФЗОП" вставлять "40.05.01
 * Национальная безопасность (ну и т.д.)
 */
@Service
public class ConfigDataService {

    /**
     * Хранит Map, содержащий настройки конфигурации для отправки клиенту
     */
    private final Map<ConfigData.ConfigType, List<ConfigData>> domainData = new HashMap<>();

    /**
     * Возвращает label (расшифровку аббревиатуры).
     *
     * @param type  тип данных
     * @param value аббревиатура в рамках данного типа (например, НБФЗОП) в рамках типа FACULTY
     * @return label - то есть расшифровку аббревиатуры
     */
    public String getDataLabel(ConfigData.ConfigType type, String value) {
        return getLabel(type, value);
    }

    /**
     * Возвращает label (расшифровку аббревиатуры).
     *
     * @param type  название типа данных в String, которое переводится непосредственно в Enum. При этом не имеет значение
     *              подчеркивания в Enum name и шрифт.
     * @param value аббревиатура в рамках данного типа (например, НБФЗОП) в рамках типа FACULTY
     * @return label - то есть расшифровку аббревиатуры
     */
    public String getDataLabel(String type, String value) {
        List<String> enumNames = Arrays.stream(ConfigData.ConfigType.values()).map(enumValue -> enumValue.name()).toList();
        ConfigData.ConfigType enumType = Arrays.stream(ConfigData.ConfigType.values())
                .filter(configType -> configType.name().replace("_", "").contains(type.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(ConfigData.ConfigType.class, value));
        return getLabel(enumType, value);
    }

    private String getLabel(ConfigData.ConfigType type, String value) {
        List<ConfigData> configData = domainData.get(type);
        List<ConfigData> collect = configData.stream().filter(config -> config.getValue().equals(value)).toList();

        if (collect.isEmpty()) {
            return value;
        }
        if (collect.get(0).getLabel().equals("отсутствует")) {
            return "";
        }
        return collect.get(0).getLabel();
    }

    public Map<ConfigData.ConfigType, List<ConfigData>> getDomainData() {
        return domainData;
    }
}
