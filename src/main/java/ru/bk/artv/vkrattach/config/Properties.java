package ru.bk.artv.vkrattach.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bk.artv.vkrattach.domain.Department;
import ru.bk.artv.vkrattach.domain.Departments;
import ru.bk.artv.vkrattach.domain.FacultiesList;
import ru.bk.artv.vkrattach.domain.YearOfRecruitmentList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "vkrattach.users")
public class Properties {

    private List<String> faculties;
    private List<String> yearOfRecruitment;

    @Bean
    YearOfRecruitmentList yearOfRecruitment() {
        System.out.println("-----------------------------------------------------");
        yearOfRecruitment.stream().forEach(e -> System.out.println(e));
        return new YearOfRecruitmentList(yearOfRecruitment);
    }

    @Bean
    FacultiesList faculties() {
        Map<String, String> facultiesMap = new HashMap<>();
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
        HashMap<String, String> departments = new HashMap<>();
        Arrays.stream(Department.values()).forEach(department -> {
            departments.put(department.name(), department.getDepartmentName());
        });
        Departments departmentsMap = new Departments();
        departmentsMap.setDepartmentsMap(departments);
        departments.keySet().stream().forEach(dep -> System.out.println(dep + " : " + departments.get(dep)));
        return departmentsMap;
    }
}
