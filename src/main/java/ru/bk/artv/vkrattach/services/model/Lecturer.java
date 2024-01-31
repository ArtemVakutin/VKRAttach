package ru.bk.artv.vkrattach.services.model;

import jakarta.persistence.*;
import lombok.Data;


/**
 * Представляет объект данных "Руководитель ВКР"
 * <p>
 * Не содержит логики. Содержится в таблице vkr_lecturer с соответсвующими полями.
 * Обязательными полями являются только имя, фамилия и кафедра.
 */
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

    @Column(name = "lecturer_telephone")
        private String telephone;

    /**
     * ученое звание
     */
    @Column(name = "lecturer_academic_degree")
    private String academicDegree;

    /**
     * ученая степень
     */
    @Column(name = "lecturer_academic_title")
    private String academicTitle;

    /**
     * кафедра
     */
    @Column(name = "lecturer_department")
    private String department;

    /**
     * звание
     */
    @Column(name = "lecturer_rank")
    private String rank;

    /**
     * вид звания
     */
    @Column(name = "lecturer_rank_type")
    private String rankType;

    /**
     * должность
     */
    @Column(name = "lecturer_position")
    private String position;
}
