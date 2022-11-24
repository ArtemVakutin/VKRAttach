package ru.bk.artv.vkrattach.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vkr_lecturer")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecturer_id")
    private Long id;

    @Column(name = "lecturer_name")
    private String name;

    @Column(name = "lecturer_surname")
    private String surname;

    @Column(name = "lecturer_patronymic")
    private String patronymic;

    @Column(name = "lecturer_email")
    private String email;

    @Column(name = "lecturer_department")
    @Enumerated(EnumType.STRING)
    private Department department;

}
