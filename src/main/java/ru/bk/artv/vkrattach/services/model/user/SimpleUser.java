package ru.bk.artv.vkrattach.services.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Объект данных User с правами доступа USER
 * <p>
 * Обязательные поля - surname, name, login (в родителе). Остальное либо заполняет сам сразу/позже, либо подгружается
 * с excel файла через соответствующий сервис.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("USER")
public class SimpleUser extends DefaultUser{

    @Column(name = "user_surname")
    private String surname;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_patronymic")
    private String patronymic;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "user_telephone")
    private String telephone;

    //специальность/направление подготовки. Из конфига.
    @Column(name = "user_faculty")
    private String faculty;

    //номер группы, только цифры (не НБФЗОП-521, а просто 521)
    @Column(name = "user_group")
    private String group;

    //год набора
    @Column(name = "user_year")
    private String year;

    //звание
    @Column(name = "user_rank")
    private String rank;

    //тип звания (полиции, юстиции и т.д)
    @Column(name = "user_rank_type")
    private String rankType;

    //должность
    @Column(name = "user_position")
    private String position;
}
