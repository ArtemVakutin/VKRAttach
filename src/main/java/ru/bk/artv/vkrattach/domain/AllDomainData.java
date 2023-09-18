package ru.bk.artv.vkrattach.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class AllDomainData {

    @Data
    @AllArgsConstructor
    public static class SimpleDomainData {
        private String value;
        private String label;
    }


    private List<SimpleDomainData> departments;
    private List<SimpleDomainData> faculties;
    private List<SimpleDomainData> years;
    private List<SimpleDomainData> ranks;
    private List<SimpleDomainData> academicTitles;
    private List<SimpleDomainData> academicDegrees;
    private List<SimpleDomainData> ranktypes;

}
