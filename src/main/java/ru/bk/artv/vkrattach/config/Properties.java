package ru.bk.artv.vkrattach.config;
import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bk.artv.vkrattach.domain.DepartmentsMap;
import ru.bk.artv.vkrattach.domain.FacultiesMap;
import ru.bk.artv.vkrattach.domain.YearsOfRecruitmentMap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@Data
@Configuration
@ConfigurationProperties(prefix = "vkrattach.users")
public class Properties {

    private List<String> faculties;
    private List<String> yearOfRecruitment;
    private Map<String, String> departments;

    @Bean
    YearsOfRecruitmentMap yearOfRecruitment() {
        log.info("YearsOfRecruitment");
        Map<String, String> years = yearOfRecruitment.stream().collect(Collectors.toMap(year -> year,
                year -> year));
        years.values().forEach(value -> log.info(value));
        return new YearsOfRecruitmentMap(years);
    }

    @Bean
    FacultiesMap faculties() {
        log.info("Faculties");
        Map<String, String> facultiesMap = new LinkedHashMap<>();
        faculties.stream().forEach(e -> {
            String[] split = e.split("=");
            facultiesMap.put(split[0], split[1]);
        });
        facultiesMap.keySet().stream().forEach(e -> log.info(e + " : " + facultiesMap.get(e)));
        return new FacultiesMap(facultiesMap);
    }

    @Bean
    DepartmentsMap departmentsMap() {
        log.info("Departments");
        departments.keySet().stream().forEach(e -> log.info(e + " : " + departments.get(e)));
        return new DepartmentsMap(departments);
    }
}
