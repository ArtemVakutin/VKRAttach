drop table if exists vkr_order;
drop table if exists vkr_lecturer;
drop table if exists vkr_theme;
drop table if exists vkr_user;

create table if not exists vkr_lecturer
(
    lecturer_id         SERIAL,
    lecturer_name       varchar(30) not null,
    lecturer_surname    varchar(30) not null,
    lecturer_patronymic varchar(30) not null,
    lecturer_email      varchar(30),
    lecturer_department varchar(30),
    PRIMARY KEY (lecturer_id)
);

create table if not exists vkr_user
(
    user_id                  SERIAL,
    user_surname             varchar(30) not null,
    user_name                varchar(30) not null,
    user_patronymic          varchar(30),
    user_email               varchar(30) not null,
    user_password            text        not null,
    user_registration_date   timestamp   default current_timestamp,
    user_telephone           varchar(30) not null,
    user_faculty             varchar(30) not null,
    user_group               varchar(30) not null,
    user_role                varchar(30) not null,
    user_deleted             boolean default false,
    user_year_of_recruitment varchar(4)  not null,
    PRIMARY KEY (user_id)
);

create table if not exists vkr_theme
(
    theme_id         SERIAL,
    theme_name       text not null,
    theme_department varchar(30),
    theme_faculty varchar(20),
    primary key (theme_id)
);

create table if not exists vkr_order
(
    order_id                 SERIAL,
    order_theme              integer   not null,
    order_preferred_lecturer integer,
    order_user               integer   not null,
    order_lecturer           integer,
    order_date               timestamp default current_timestamp,
    order_accept             boolean   not null,
    order_accept_date        timestamp,
    PRIMARY KEY (order_id),
    FOREIGN KEY (order_user) REFERENCES vkr_user (user_id) ON delete restrict,
    FOREIGN KEY (order_lecturer) REFERENCES vkr_lecturer (lecturer_id) ON delete restrict,
    FOREIGN KEY (order_preferred_lecturer) REFERENCES vkr_lecturer (lecturer_id) ON delete restrict,
    FOREIGN KEY (order_theme) REFERENCES vkr_theme (theme_id) ON delete restrict
);



