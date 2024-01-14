package ru.bk.artv.vkrattach.testutils;

import lombok.Data;
import ru.bk.artv.vkrattach.services.model.user.AdminUser;
import ru.bk.artv.vkrattach.services.model.user.ModeratorUser;
import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;

import java.time.LocalDateTime;

public class UserBuilder {

    private Long id = 1L;
    private String login = "user1";
    private String password = "11111";
    private Role role = Role.USER;
    private String surname = "Шадринский";
    private String name = "Иван";
    private String patronymic = "Петрович";
    private String email = "afd@lad.ru";
    private LocalDateTime registrationDate = LocalDateTime.now();
    private String telephone = "904594044924";
    private String faculty = "НБФЗОП";
    private String group = "593";
    private String year = "2019";
    private String rank = "лейтенант";
    private String rankType = "полиции";
    private String position = "слушатель";
    private String department = "УПв";

    private UserBuilder() {
    }

    public static UserBuilder create() {
        return new UserBuilder();
    }

    public UserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder withLogin(String login) {
        this.login = login;
        return this;
    }

    public UserBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public UserBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public UserBuilder withFaculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    public UserBuilder withGroup(String group) {
        this.group = group;
        return this;
    }

    public UserBuilder withYear(String year) {
        this.year = year;
        return this;
    }

    public UserBuilder withRank(String rank) {
        this.rank = rank;
        return this;
    }

    public UserBuilder withRankType(String rankType) {
        this.rankType = rankType;
        return this;
    }

    public UserBuilder withPosition(String position) {
        this.position = position;
        return this;
    }

    public UserBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public SimpleUser buildSimpleUser() {
        SimpleUser user = new SimpleUser();
        user.setId(id);
        user.setLogin(login);
        user.setSurname(surname);
        user.setName(name);
        user.setPatronymic(patronymic);
        user.setEmail(email);
        user.setPassword(password);
        user.setFaculty(faculty);
        user.setYear(year);
        user.setTelephone(telephone);
        user.setGroup(group);
        user.setRank(rank);
        user.setRankType(rankType);
        user.setPosition(position);
        user.setRole(Role.USER);
        return user;
    }

    public AdminUser buildAdminUser() {
        AdminUser user = new AdminUser();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(Role.ADMIN);
        return user;
    }

    public ModeratorUser buildModeratorUser() {
        ModeratorUser user = new ModeratorUser();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setDepartment(department);
        user.setRole(Role.MODERATOR);
        return user;
    }


}