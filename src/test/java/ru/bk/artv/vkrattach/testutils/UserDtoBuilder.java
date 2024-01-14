package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.web.dto.UserDto;

public class UserDtoBuilder {
    private Long id = null;
    private String login = "user1";
    private String surname = "Шадрицкий";
    private String  name =  "Иван";
    private String patronymic = "Викторович";
    private String email = "shadr@bk.ru";
    private String oldPassword;
    private String password = "11111";
    private String faculty = "НБФЗОП";
    private String year = "2019";
    private String telephone = "902392032323";
    private String group = "593";
    private String department = "УПв";
    private String rank = "лейтенант";
    private String rankType = "полиции";
    private String position = "слушатель";
    private String exception;
    private Role role = Role.USER;

    private UserDtoBuilder() {
    }

    public static UserDtoBuilder create() {
        return new UserDtoBuilder();
    }

    public UserDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserDtoBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public UserDtoBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public UserDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserDtoBuilder withPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public UserDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserDtoBuilder withOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public UserDtoBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserDtoBuilder withFaculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    public UserDtoBuilder withYear(String year) {
        this.year = year;
        return this;
    }

    public UserDtoBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public UserDtoBuilder withGroup(String group) {
        this.group = group;
        return this;
    }

    public UserDtoBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public UserDtoBuilder withRank(String rank) {
        this.rank = rank;
        return this;
    }

    public UserDtoBuilder withRankType(String rankType) {
        this.rankType = rankType;
        return this;
    }

    public UserDtoBuilder withPosition(String position) {
        this.position = position;
        return this;
    }

    public UserDtoBuilder withException(String exception) {
        this.exception = exception;
        return this;
    }

    public UserDtoBuilder withRole(Role role) {
        this.role = role;
        return this;
    }
    public UserDto build() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setLogin(login);
        userDto.setSurname(surname);
        userDto.setName(name);
        userDto.setPatronymic(patronymic);
        userDto.setEmail(email);
        userDto.setOldPassword(oldPassword);
        userDto.setPassword(password);
        userDto.setFaculty(faculty);
        userDto.setYear(year);
        userDto.setTelephone(telephone);
        userDto.setGroup(group);
        userDto.setDepartment(department);
        userDto.setRank(rank);
        userDto.setRankType(rankType);
        userDto.setPosition(position);
        userDto.setException(exception);
        userDto.setRole(role);
        return userDto;
    }
}