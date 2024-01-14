package ru.bk.artv.vkrattach.services.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Util-класс
 * Используется для логирования Map-ов
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapToStringUtil {

    public static String mapAsString(Map<String, String[]> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + Arrays.toString(map.get(key)))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    // TODO: 08.12.2023 Добавить возможность перевода Map<Object, List<Object>, List<Object> и т.д.
}
