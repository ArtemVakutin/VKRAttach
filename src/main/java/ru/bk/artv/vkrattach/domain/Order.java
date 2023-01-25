package ru.bk.artv.vkrattach.domain;


import lombok.Data;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "vkr_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_theme")
    private Theme theme;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_user")
    private SimpleUser user;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_lecturer")
    private Lecturer lecturer;

    @Column(name = "order_date")
    private LocalDate requestDate;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.ORDINAL)
    private OrderStatus orderStatus;

    @Column(name = "order_accept_date")
    private LocalDate requestStatusDate;

    @Column(name = "order_comments")
    private String comments;

    public enum OrderStatus {
        UNDER_CONSIDERATION, NEED_CHANGES, REFUSED, ACCEPTED
    }
}
