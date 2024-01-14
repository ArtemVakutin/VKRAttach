package ru.bk.artv.vkrattach.services.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


/**
 * Представляет объект данных "Тема ВКР"
 * <p>
 * Содержит данные для хранения информации о теме ВКР. Не содержит бизнес-логики
 * Все поля обязательны при сохранении
 */
@Data
@Entity
@Table(name = "vkr_theme")
public class Theme {

    /**
     * айдишник
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long themeId;

    /**
     * Наименование темы
     */
    @Column(name = "theme_name")
    private String themeName;

    /**
     * кафедра
     */
    @Column(name = "theme_department")
    private String department;

    /**
     * специальность (направление подготовки), включая форму обучения
     */
    @Column(name="theme_faculty")
    private String faculty;


    /**
     * год набора
     */
    @Column(name="theme_year")
    private String year;

    /**
     * перечень заявок, в которых эта тема закреплена (может быть несколько с учетом того, что часть может быть отклонена)
     */
    @OneToMany(mappedBy = "theme")
    private List<Order> orders;


    // Переопределен, чтобы не выбрасывало StackOverFlow в связи с наличием списка заявок
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
