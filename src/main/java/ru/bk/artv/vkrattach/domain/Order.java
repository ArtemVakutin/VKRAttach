package ru.bk.artv.vkrattach.domain;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "order_preferred_lecturer")
    private Lecturer preferredLecturer;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_user", insertable = false, updatable = false)
    private User user;

    @ManyToOne (cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn (name = "order_user")
    private Lecturer lecturer;

    @Column(name = "order_date")
    private LocalDateTime requestDateTime;

    @Column(name = "order_accept")
    private Boolean requestAccept;

    @Column(name = "order_accept_date")
    private LocalDateTime requestAcceptDateTime;

}
