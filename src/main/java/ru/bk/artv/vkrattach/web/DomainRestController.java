package ru.bk.artv.vkrattach.web;

import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.domain.*;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "rest/domain")
public class DomainRestController {

DepartmentsMap departments;
FacultiesMap faculties;
YearsOfRecruitmentMap years;

    public DomainRestController(DepartmentsMap departments, FacultiesMap faculties, YearsOfRecruitmentMap years) {
        this.departments = departments;
        this.faculties = faculties;
        this.years = years;
    }

    @GetMapping(path = "/departments")
    public Map<String, String> getDepartments() {
        return departments.getDepartments();
    }

    @GetMapping(path = "/faculties")
    public Map<String, String> getFacultiesWeb() {
        return faculties.getFaculties();
    }

    @GetMapping(path = "/years")
    public Map<String, String> getYearsOfRecruitmentWeb() {
        return years.getYears();
    }

    @GetMapping(path = "/ranks")
    public Map<String, String> getRanks(){
        return Arrays.stream(Rank.values()).collect(Collectors.toMap(Enum::name, Rank::getRank));
    }

    @GetMapping(path = "/academictitles")
    public Map<String, String> getAcademicTitles(){
        return Arrays.stream(Lecturer.AcademicTitle.values()).collect(Collectors.toMap(Enum::name,
                Lecturer.AcademicTitle::getName));
    }

    @GetMapping(path = "/academicdegrees")
    public Map<String, String> getAcademicDegrees(){
        return Arrays.stream(Lecturer.AcademicDegree.values()).collect(Collectors.toMap(Enum::name,
                Lecturer.AcademicDegree::getName));
    }

    @GetMapping(path = "/ranktypes")
    public Map<String, String> getRankTypes(){
        return Arrays.stream(Rank.RankType.values()).collect(Collectors.toMap(Enum::name,
                Rank.RankType::getRankType));
    }

}
