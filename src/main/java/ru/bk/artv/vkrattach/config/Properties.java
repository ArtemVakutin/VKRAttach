package ru.bk.artv.vkrattach.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bk.artv.vkrattach.domain.Department;
import ru.bk.artv.vkrattach.domain.Departments;
import ru.bk.artv.vkrattach.domain.FacultiesList;
import ru.bk.artv.vkrattach.domain.YearOfRecruitmentList;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Configuration
@ConfigurationProperties(prefix = "vkrattach.users")
public class Properties {

    private List<String> faculties;
    private List<String> yearOfRecruitment;

    @Bean
    YearOfRecruitmentList yearOfRecruitment() {
        System.out.println("-----------------------------------------------------");
        final int i[] = {1};
        Map<String, String> years = yearOfRecruitment.stream().collect(Collectors.toMap((year) -> {
//            final int a = i[0]++;
            String m = (i[0]++) + "";
            return m;
                },
                year->year));
        years.values().forEach(value-> System.out.println(value));
        return new YearOfRecruitmentList(years);
    }

    @Bean
    FacultiesList faculties() {
        Map<String, String> facultiesMap = new LinkedHashMap<>();
        faculties.stream().forEach(e -> {
            String[] split = e.split("=");
            facultiesMap.put(split[0], split[1]);
        });

        System.out.println("-----------------------------------------------------");
        facultiesMap.values().stream().forEach(e -> System.out.println(e));
        facultiesMap.keySet().stream().forEach(e -> System.out.println(e));

        return new FacultiesList(facultiesMap);
    }


    @Bean
    Departments departmentsMap() {
        Map<String, String> departments = new LinkedHashMap<>();
        Arrays.stream(Department.values()).forEach(department -> {
            departments.put(department.name(), department.getDepartmentName());
        });
        Departments departmentsMap = new Departments();
        departmentsMap.setDepartmentsMap(departments);
        departments.keySet().stream().forEach(dep -> System.out.println(dep + " : " + departments.get(dep)));
        return departmentsMap;
    }
}
