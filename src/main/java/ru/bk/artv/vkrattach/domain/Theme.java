package ru.bk.artv.vkrattach.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "vkr_theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long themeId;

    @Column(name = "theme_name")
    private String themeName;

    @Column(name = "theme_department")
    @Enumerated(EnumType.STRING)
    private Department themeDepartment;

    @Column(name="theme_faculty")
    private String faculty;

    @Column(name="theme_year_of_recruitment")
    private String yearOfRecruitment;

}
