package ru.bk.artv.vkrattach.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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
    private String department;

    @Column(name="theme_faculty")
    private String faculty;

    @Column(name="theme_year")
    private String year;

    @OneToMany(mappedBy = "theme")
    private List<Order> orders;

    @Override
    public String toString() {
        return "Theme{" +
                "themeId=" + themeId +
                ", themeName='" + themeName + '\'' +
                ", department='" + department + '\'' +
                ", faculty='" + faculty + '\'' +
                ", year='" + year + '\'' +
                '}';
    }
}
