package ru.bk.artv.vkrattach.domain.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

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

    @Column(name = "user_faculty")
    private String faculty;

    @Column(name = "user_group")
    private String group;

    @Column(name = "user_year")
    private String year;

    @Column(name = "user_rank")
    private String rank;

    @Column(name = "user_rank_type")
    private String rankType;

    @Column(name = "user_position")
    private String position;
}
