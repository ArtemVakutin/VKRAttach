package ru.bk.artv.vkrattach.services.model;


import jakarta.persistence.*;
import lombok.Data;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;

import java.time.LocalDate;


/**
 * Представляет объект данных "Заявка на ВКР"
 * <p>
 * Не содержит логики. Содержится в таблице vkr_order. Является базовым классом для остальных.
 */
@Data
@Entity
@Table(name = "vkr_order")
public class Order {

    /**
     * айдишник, генерируется базой данных
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;


    /**
     * Темы. Одна заявка - одна тема. Наоборот может быть много.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_theme")
    private Theme theme;

    /**
     * Пользователь, за которым закреплена тема
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_user")
    private SimpleUser user;

    /**
     * Руководитель вкр
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_lecturer")
    private Lecturer lecturer;

    /**
     * Дата поступления заявки
     */
    @Column(name = "order_date")
    private LocalDate requestDate;

    /**
     * Статус заявки
     */
    @Column(name = "order_status")
    @Enumerated(value = EnumType.ORDINAL)
    private RequestStatus requestStatus;

    /**
     * Дата, когда заявка одобрена (отклонена)
     */
    @Column(name = "order_accept_date")
    private LocalDate requestStatusDate;

    /**
     * Комментарий (не обязательно)
     */
    @Column(name = "order_comments")
    private String comments;

    /**
     * Статусы заявок
     */
    public enum RequestStatus {
        UNDER_CONSIDERATION, REFUSED, ACCEPTED
    }
}
