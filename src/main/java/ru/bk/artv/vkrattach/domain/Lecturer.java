package ru.bk.artv.vkrattach.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "vkr_lecturer")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecturer_id")
    private Long id;

    @Column(name = "lecturer_name")
    @NotBlank(message = "Поле \"Имя\" не должно быть пустым")
    private String name;

    @Column(name = "lecturer_surname")
    @NotBlank(message = "Поле \"Фамилия\" не должно быть пустым")
    private String surname;

    @Column(name = "lecturer_patronymic")
    private String patronymic;

    @Column(name = "lecturer_email")
    @NotBlank(message = "Поле \"e-mail\" не должно быть пустым")
    private String email;

    @Column(name = "lecturer_telephone")
        private String telephone;

    @Column(name = "lecturer_academic_degree")
    @Enumerated
    @NotNull(message = "Выберите ученую степень (при отсутствии выберите \"Отсутствует\"")
    private AcademicDegree academicDegree;

    @Column(name = "lecturer_academic_title")
    @Enumerated
    @NotNull(message = "Выберите ученое звание (при отсутствии выберите \"Отсутствует\"")
    private AcademicTitle academicTitle;

    @Column(name = "lecturer_department")
    @NotBlank(message = "Выберите кафедру")
    private String department;

    @Column(name = "lecturer_rank")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Выберите специальное звание (при отсутствии выберите \"Отсутствует\"")
    private Rank rank;

    @Column(name = "lecturer_deleted")
    boolean deleted = false;


    public enum AcademicDegree{
        NONE("отсутствует"), CANDIDATE("кандидат юридических наук"), DOCTOR("доктор юридических наук");
        final String name;

        AcademicDegree(String name) {
            this.name = name;
        }

                public String getName() {
            return name;
        }
    }

    public enum AcademicTitle{
        NONE("отсутствует"), DOCENT("доцент"), PROFESSOR("профессор");
        final String name;

        AcademicTitle(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
