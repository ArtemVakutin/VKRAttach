package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.web.dto.LecturerDto;
import ru.bk.artv.vkrattach.web.dto.LecturerFullDto;

public class LecturerDtoBuilder {

    private Long id = null;
    private String name = "Иван";
    private String surname = "Иванов";
    private String patronymic = "Иванович";
    private String department = "УПв";
    private String email = "ivan@example.com";
    private String telephone = "905944444444";
    private String academicDegree = "к.ю.н.";
    private String academicTitle = "доцент";
    private String rank = "майор";
    private String exception;
    private String position = "преподаватель";
    private int ordersCount = 0;

    private LecturerDtoBuilder() {
    }

    public static LecturerDtoBuilder create() {
        return new LecturerDtoBuilder();
    }

    public LecturerDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LecturerDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LecturerDtoBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public LecturerDtoBuilder withPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public LecturerDtoBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public LecturerDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public LecturerDtoBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public LecturerDtoBuilder withAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
        return this;
    }

    public LecturerDtoBuilder withAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
        return this;
    }

    public LecturerDtoBuilder withRank(String rank) {
        this.rank = rank;
        return this;
    }

    public LecturerDtoBuilder withException(String exception) {
        this.exception = exception;
        return this;
    }

    public LecturerDtoBuilder withPosition(String position) {
        this.position = position;
        return this;
    }

    public LecturerDtoBuilder withOrdersCount(int ordersCount) {
        this.ordersCount = ordersCount;
        return this;
    }

    public LecturerFullDto buildLecturerFullDto() {
        LecturerFullDto lecturerFullDto = new LecturerFullDto();
        lecturerFullDto.setId(id);
        lecturerFullDto.setName(name);
        lecturerFullDto.setSurname(surname);
        lecturerFullDto.setPatronymic(patronymic);
        lecturerFullDto.setDepartment(department);
        lecturerFullDto.setEmail(email);
        lecturerFullDto.setTelephone(telephone);
        lecturerFullDto.setAcademicDegree(academicDegree);
        lecturerFullDto.setAcademicTitle(academicTitle);
        lecturerFullDto.setRank(rank);
        lecturerFullDto.setException(exception);
        lecturerFullDto.setPosition(position);
        lecturerFullDto.setOrdersCount(ordersCount);
        return lecturerFullDto;
    }

    public LecturerDto buildLecturerDto() {
        LecturerDto lecturerDto = new LecturerDto();
        lecturerDto.setId(id);
        lecturerDto.setName(name);
        lecturerDto.setSurname(surname);
        lecturerDto.setPatronymic(patronymic);
        lecturerDto.setDepartment(department);
        return lecturerDto;
    }
}