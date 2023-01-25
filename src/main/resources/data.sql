INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('ADMIN',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
         'ADMIN');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('UPV',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('UPR',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

INSERT INTO public.vkr_user(
    user_login, user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment, user_rank, user_rank_type)
VALUES ('IVANOV',
        'Иванов',
        'Иван',
        'Иванович',
        'artv@bk.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '90419123123',
        'НБФЗОП',
        '596',
        'USER',
        '2018',
        'COLONEL',
        'POLICE');

INSERT INTO public.vkr_user(
    user_login, user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment, user_rank, user_rank_type)
VALUES ('PETROV',
        'Петров',
        'Петр',
        'Петрович',
        'p@p.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904191234123',
        'НБФЗОП',
        '584',
        'USER',
        '2019',
        'COLONEL',
        'POLICE');

INSERT INTO public.vkr_user(
    user_login, user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment, user_rank, user_rank_type)
VALUES ('VASILIEV',
        'Васильев',
        'Василий',
        'Васильевич',
        'v@v.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904191234123',
        'НБФЗОП',
        '584',
        'MODERATOR',
        '2019',
        'COLONEL',
        'POLICE');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('доение коров в особых условиях', 'UPV', 'НБФЗОП', '2018');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('поедание кобыл на завтрак', 'UPV', 'НБФЗОП', '2019');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('прыжки в воду с самолета без парашюта', 'UPR', 'НБФЗОП', '2018');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('кидание апельсинов на скорость', 'UPR', 'НБФЗОП', '2019');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('доение коров в особых условиях', 'UPV', 'ЮВФЗОМ','2018');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('поедание кобыл на завтрак', 'UPV', 'ЮВФЗОМ', '2018');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('прыжки в воду с самолета без парашюта', 'UPR', 'ЮВФЗОМ', '2018');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty, theme_year_of_recruitment)
VALUES ('кидание апельсинов на скорость', 'UPR', 'ЮВФЗОМ','2019');

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department, lecturer_telephone, lecturer_academic_degree, lecturer_academic_title, lecturer_rank, lecturer_deleted)
VALUES ('Злобнопреподов', 'Артем', 'Артемович', 'mail@mail.ru', 'UPV', '905944444444', 1, 2,'MAJOR', false);

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department, lecturer_telephone, lecturer_academic_degree, lecturer_academic_title,lecturer_rank, lecturer_deleted)
VALUES ('Иванов', 'Иван', 'Иванович', 'mail@mail.ru', 'UPV', '905944444444', 2, 1, 'MAJOR', false);

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department, lecturer_telephone, lecturer_academic_degree, lecturer_academic_title,lecturer_rank, lecturer_deleted)
VALUES ('Петров', 'Петр', 'Петрович', 'mail@mail.ru', 'UPV', '905944444444', 2, 1,'COLONEL', false);

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department, lecturer_telephone, lecturer_academic_degree, lecturer_academic_title,lecturer_rank, lecturer_deleted)
VALUES ('Поросенков', 'Поросенок', 'Поросятович', 'mail@mail.ru', 'UPV', '905944444444', 2, 1,'COLONEL', true);

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department, lecturer_telephone, lecturer_academic_degree, lecturer_academic_title,lecturer_rank, lecturer_deleted)
VALUES ('Добропреподов', 'Илья', 'Ильич', 'mail1@mail1.ru', 'UPR', '905944444444', 1, 2, 'COLONEL', false);

INSERT INTO public.vkr_order(
    order_theme, order_user, order_lecturer, order_date, order_status, order_accept_date, order_comments)
VALUES ('1', '4', '1', '2022-11-03', 0, '2022-11-03', 'Переделать');

INSERT INTO public.vkr_order(
    order_theme, order_user, order_lecturer, order_date, order_status, order_accept_date, order_comments)
VALUES ('1', '4', '1', '2022-11-03', 1, '2022-11-03', 'Комментарий раз');

INSERT INTO public.vkr_order(
    order_theme, order_user, order_lecturer, order_date, order_status, order_accept_date, order_comments)
VALUES ('1', '4', '1', '2022-11-03', 2, '2022-11-03', 'Комментарий два');

INSERT INTO public.vkr_order(
    order_theme, order_user, order_lecturer, order_date, order_status, order_accept_date, order_comments)
VALUES ('1', '5', '1', '2022-11-03', 3, '2022-11-03', 'Комментарий три');

INSERT INTO public.vkr_order(
    order_theme, order_user, order_lecturer, order_date, order_status, order_accept_date, order_comments)
VALUES ('1', '5', null,'2022-11-03', 3, '2022-12-01', 'Принято');