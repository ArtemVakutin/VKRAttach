package ru.bk.artv.vkrattach.config;

import lombok.Data;
import lombok.extern.java.Log;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bk.artv.vkrattach.domain.AllDomainData;
import ru.bk.artv.vkrattach.domain.AllDomainData.SimpleDomainData;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.domain.Rank;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Data
@Configuration
@ConfigurationProperties(prefix = "vkrattach.users")
public class Properties {

    private List<String> faculties;
    private List<String> yearOfRecruitment;
    private List<String> departments;


    @Bean
    public AllDomainData allDomainData() {
        AllDomainData allDomainData = new AllDomainData();

        allDomainData.setYears(makeYears());
        allDomainData.setFaculties(makeFaculties());
        allDomainData.setDepartments(makeDeparments());
        allDomainData.setAcademicDegrees(makeAcademicDegrees());
        allDomainData.setAcademicTitles(makeAcademicTitles());
        allDomainData.setRanks(makeRanks());
        allDomainData.setRanktypes(makeRankTypes());



        return allDomainData;
    }

    private List<SimpleDomainData> makeRankTypes() {
        return Arrays.stream(Rank.RankType.values())
                .map(value->new SimpleDomainData(value.name(), value.getRankType()))
                .collect(Collectors.toList());
    }

    private List<SimpleDomainData> makeRanks() {
        return Arrays.stream(Rank.values())
                .map(value->new SimpleDomainData(value.name(), value.getRank()))
                .collect(Collectors.toList());
    }

    private List<AllDomainData.SimpleDomainData> makeAcademicDegrees() {
        return Arrays.stream(Lecturer.AcademicDegree.values())
                .map(value->new SimpleDomainData(value.name(), value.getName()))
                .collect(Collectors.toList());
    }

    private List<AllDomainData.SimpleDomainData> makeAcademicTitles() {
        return Arrays.stream(Lecturer.AcademicTitle.values())
                .map(value->new SimpleDomainData(value.name(), value.getName()))
                .collect(Collectors.toList());
    }


    private List<AllDomainData.SimpleDomainData> makeYears() {
        return yearOfRecruitment.stream().map((year) -> new SimpleDomainData(year, year)).collect(Collectors.toList());
    }

    private List<AllDomainData.SimpleDomainData> makeFaculties() {
        return faculties.stream().map((faculty) -> {
            String[] split = faculty.split("=");
             return new SimpleDomainData(split[0], split[1]);
        }).collect(Collectors.toList());
    }

    private List<AllDomainData.SimpleDomainData> makeDeparments() {
        return departments.stream().map((faculty) -> {
            String[] split = faculty.split("=");
            return new SimpleDomainData(split[0], split[1]);
        }).collect(Collectors.toList());
    }


}
