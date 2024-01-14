package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.web.dto.ThemeDto;

public class ThemeDtoBuilder {
    private Long id = null;
    private String themeName = "Перспективы развития науки уголовного права.";
    private String department = "УПв";
    private String faculty = "НБФЗОП";
    private String year = "2019";
    private boolean isBusy = false;

    private ThemeDtoBuilder() {
    }

    public static ThemeDtoBuilder create() {
        return new ThemeDtoBuilder();
    }

    public ThemeDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ThemeDtoBuilder withThemeName(String themeName) {
        this.themeName = themeName;
        return this;
    }

    public ThemeDtoBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public ThemeDtoBuilder withFaculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    public ThemeDtoBuilder withYear(String year) {
        this.year = year;
        return this;
    }

    public ThemeDtoBuilder withIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
        return this;
    }

    public ThemeDto build() {
        ThemeDto themeDTO = new ThemeDto();
        themeDTO.setId(id);
        themeDTO.setThemeName(themeName);
        themeDTO.setDepartment(department);
        themeDTO.setFaculty(faculty);
        themeDTO.setYear(year);
        themeDTO.setBusy(isBusy);
        return themeDTO;
    }
}