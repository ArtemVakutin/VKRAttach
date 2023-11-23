drop table if exists vkr_order;
drop table if exists vkr_lecturer;
drop table if exists vkr_theme;
drop table if exists vkr_user;
drop table if exists vkr_config_data;
drop table if exists vkr_deactivated_token;

create table if not exists vkr_lecturer
(
    lecturer_id              SERIAL,
    lecturer_name            varchar(30) not null,
    lecturer_surname         varchar(30) not null,
    lecturer_patronymic      varchar(30) not null,
    lecturer_email           varchar(30),
    lecturer_department      varchar(30) not null,
    lecturer_telephone       varchar(20),
    lecturer_academic_degree varchar(30),
    lecturer_academic_title  varchar(30),
    lecturer_rank            varchar(30),
    lecturer_rank_type       varchar(30),
    lecturer_position        varchar(30),
    CONSTRAINT lecturer_data_unique UNIQUE (lecturer_department, lecturer_name, lecturer_surname, lecturer_patronymic),
    PRIMARY KEY (lecturer_id)
);

create table if not exists vkr_user
(
    user_id                SERIAL,
    user_login             varchar(30) not null UNIQUE,
    user_surname           varchar(30),
    user_name              varchar(30),
    user_patronymic        varchar(30),
    user_email             varchar(30),
    user_password          text        not null,
    user_registration_date timestamp            default current_timestamp,
    user_telephone         varchar(30),
    user_faculty           varchar(30),
    user_group             varchar(30),
    user_role              varchar(10) not null DEFAULT 'USER',
    user_year              varchar(4),
    user_rank              varchar(30),
    user_rank_type         varchar(30),
    user_position          varchar(30),
    user_department        varchar(30),
    PRIMARY KEY (user_id)
);

create table if not exists vkr_theme
(
    theme_id         SERIAL,
    theme_name       text        not null,
    theme_department varchar(30) not null,
    theme_faculty    varchar(20) not null,
    theme_year       varchar(4)  not null,
    CONSTRAINT theme_data_unique UNIQUE (theme_department, theme_faculty, theme_year, theme_name),
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

create table if not exists vkr_orders_max_count
(
    max_count_id         SERIAL,
    max_count_faculty    varchar(30) not null,
    max_count_department varchar(30) not null,
    max_count_year       varchar(30) not null,
    PRIMARY KEY (max_count_id),
    CONSTRAINT config_data_unique UNIQUE (max_count_faculty, max_count_department, max_count_year)
);

create table if not exists vkr_config_data
(
    config_id         SERIAL,
    config_type       varchar(30)  not null,
    config_value      varchar(100) not null,
    config_label      varchar(100) not null,
    config_deprecated boolean default false,
    PRIMARY KEY (config_id),
    CONSTRAINT config_data_unique UNIQUE (config_type, config_value)
);

create table vkr_deactivated_token
(
    id         uuid primary key,
    keep_until timestamp not null check ( keep_until > now() )
);



