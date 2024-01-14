CREATE TABLE IF NOT EXISTS vkr_deactivated_token
(
    token_id         UUID PRIMARY KEY,
    token_keep_until TIMESTAMP
);

CREATE TABLE IF NOT EXISTS VKR_LECTURER
(
    lecturer_id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    lecturer_name            VARCHAR(30) NOT NULL,
    lecturer_surname         VARCHAR(30) NOT NULL,
    lecturer_patronymic      VARCHAR(30) NOT NULL,
    lecturer_email           VARCHAR(30),
    lecturer_department      VARCHAR(30) NOT NULL,
    lecturer_telephone       VARCHAR(20),
    lecturer_academic_degree VARCHAR(30),
    lecturer_academic_title  VARCHAR(30),
    lecturer_rank            VARCHAR(30),
    lecturer_rank_type       VARCHAR(30),
    lecturer_position        VARCHAR(30),
    CONSTRAINT lecturer_data_unique UNIQUE (lecturer_department, lecturer_name, lecturer_surname, lecturer_patronymic)
);

CREATE TABLE IF NOT EXISTS vkr_user
(
    user_id                INT AUTO_INCREMENT PRIMARY KEY,
    user_login             VARCHAR(30) NOT NULL UNIQUE,
    user_surname           VARCHAR(30),
    user_name              VARCHAR(30),
    user_patronymic        VARCHAR(30),
    user_email             VARCHAR(30),
    user_password          TEXT        NOT NULL,
    user_registration_date TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    user_telephone         VARCHAR(30),
    user_faculty           VARCHAR(30),
    user_group             VARCHAR(30),
    user_role              VARCHAR(10) NOT NULL DEFAULT 'USER',
    user_year              VARCHAR(4),
    user_rank              VARCHAR(30),
    user_rank_type         VARCHAR(30),
    user_position          VARCHAR(30),
    user_department        VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS vkr_theme
(
    theme_id         INT AUTO_INCREMENT PRIMARY KEY,
    theme_name       TEXT        NOT NULL,
    theme_department VARCHAR(30) NOT NULL,
    theme_faculty    VARCHAR(20) NOT NULL,
    theme_year       VARCHAR(4)  NOT NULL,
    CONSTRAINT theme_data_unique UNIQUE (theme_department, theme_faculty, theme_year, theme_name)
);

CREATE TABLE IF NOT EXISTS vkr_order (
                                         order_id          INT AUTO_INCREMENT PRIMARY KEY,
                                         order_theme       INT NOT NULL,
                                         order_user        INT NOT NULL,
                                         order_lecturer    INT,
                                         order_date        DATE,
                                         order_status      INT NOT NULL,
                                         order_accept_date DATE,
                                         order_comments    TEXT,
                                         FOREIGN KEY (order_user) REFERENCES vkr_user (user_id) ON DELETE RESTRICT,
                                         FOREIGN KEY (order_lecturer) REFERENCES vkr_lecturer (lecturer_id) ON DELETE RESTRICT,
                                         FOREIGN KEY (order_theme) REFERENCES vkr_theme (theme_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS vkr_orders_max_count (
                                                    max_count_id         INT AUTO_INCREMENT PRIMARY KEY,
                                                    max_count_faculty    VARCHAR(30) NOT NULL,
                                                    max_count_department VARCHAR(30) NOT NULL,
                                                    max_count_year       VARCHAR(30) NOT NULL,
                                                    CONSTRAINT config_max_count_unique UNIQUE (max_count_faculty, max_count_department, max_count_year)
);

CREATE TABLE IF NOT EXISTS vkr_config_data (
                                               config_id         INT AUTO_INCREMENT PRIMARY KEY,
                                               config_type       VARCHAR(30) NOT NULL,
                                               config_value      VARCHAR(100) NOT NULL,
                                               config_label      VARCHAR(100) NOT NULL,
                                               config_deprecated BOOLEAN DEFAULT FALSE,
                                               CONSTRAINT config_data_unique UNIQUE (config_type, config_value)
);