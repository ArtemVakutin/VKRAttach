package ru.bk.artv.vkrattach.testutils;

import ru.bk.artv.vkrattach.services.model.Lecturer;

public class LecturerBuilder {

    private Long id = null;
    private String name = "Иван";
    private String surname = "Иванов";
    private String patronymic = "Иванович";
    private String email = "ivan@example.com";
    private String telephone = "905944444444";
    private String academicDegree = "к.ю.н.";
    private String academicTitle = "доцент";
    private String department = "УПв";
    private String rank = "майор";
    private String position = "преподаватель";

    private LecturerBuilder() {
        // Конструктор приватный, так как мы хотим, чтобы объект создавался только через статический метод create()
    }

    // Мы используем метод create(), чтобы создать экземпляр этого Builder
    public static LecturerBuilder create() {
        return new LecturerBuilder();
    }

    // Методы для установки значений полей
    public LecturerBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LecturerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LecturerBuilder withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public LecturerBuilder withPatronymic(String patronymic) {
        this.patronymic = patronymic;
        return this;
    }

    public LecturerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public LecturerBuilder withTelephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public LecturerBuilder withAcademicDegree(String academicDegree) {
        this.academicDegree = academicDegree;
        return this;
    }

    public LecturerBuilder withAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
        return this;
    }

    public LecturerBuilder withDepartment(String department) {
        this.department = department;
        return this;
    }

    public LecturerBuilder withRank(String rank) {
        this.rank = rank;
        return this;
    }

    public LecturerBuilder withPosition(String position) {
        this.position = position;
        return this;
    }

    // Метод build() для создания объекта Lecturer с установленными значениями полей
    public Lecturer build() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(id);
        lecturer.setName(name);
        lecturer.setSurname(surname);
        lecturer.setPatronymic(patronymic);
        lecturer.setEmail(email);
        lecturer.setTelephone(telephone);
        lecturer.setAcademicDegree(academicDegree);
        lecturer.setAcademicTitle(academicTitle);
        lecturer.setDepartment(department);
        lecturer.setRank(rank);
        lecturer.setPosition(position);

        return lecturer;
    }
}