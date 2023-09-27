package ru.bk.artv.vkrattach.services.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapToStringUtil {

    public static String mapAsString(Map<String, String[]> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + Arrays.toString(map.get(key)))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}
