drop table if exists vkr_order;
drop table if exists vkr_lecturer;
drop table if exists vkr_theme;
drop table if exists vkr_user;

create table if not exists vkr_lecturer
(
    lecturer_id              SERIAL,
    lecturer_name            varchar(30) not null,
    lecturer_surname         varchar(30) not null,
    lecturer_patronymic      varchar(30) not null,
    lecturer_email           varchar(30),
    lecturer_department      varchar(30) not null,
    lecturer_telephone       varchar(20),
    lecturer_academic_degree integer,
    lecturer_academic_title  integer,
    lecturer_rank            varchar(30),
    PRIMARY KEY (lecturer_id)
);

create table if not exists vkr_user
(
    user_id                  SERIAL,
    user_login               varchar(30) not null UNIQUE,
    user_surname             varchar(30),
    user_name                varchar(30),
    user_patronymic          varchar(30),
    user_email               varchar(30) UNIQUE,
    user_password            text        not null,
    user_registration_date   timestamp            default current_timestamp,
    user_telephone           varchar(30),
    user_faculty             varchar(30),
    user_group               varchar(30),
    user_role                varchar(10) not null DEFAULT 'USER',
    user_deleted             boolean              default false,
    user_year_of_recruitment varchar(4),
    user_rank                varchar(30),
    user_rank_type           varchar(30),
    PRIMARY KEY (user_id)
);

create table if not exists vkr_theme
(
    theme_id                  SERIAL,
    theme_name                text        not null,
    theme_department          varchar(30) not null,
    theme_faculty             varchar(20) not null,
    theme_year_of_recruitment varchar(4)  not null,
    primary key (theme_id)
);

create table if not exists vkr_order
(
    order_id          SERIAL,
    order_theme       integer not null,
    order_user        integer not null,
    order_lecturer    integer,
    order_date        date,
    order_status      integer not null,
    order_accept_date date,
    order_comments    text,
    PRIMARY KEY (order_id),
    FOREIGN KEY (order_user) REFERENCES vkr_user (user_id) ON delete restrict,
    FOREIGN KEY (order_lecturer) REFERENCES vkr_lecturer (lecturer_id) ON delete restrict,
    FOREIGN KEY (order_theme) REFERENCES vkr_theme (theme_id) ON delete restrict
);



