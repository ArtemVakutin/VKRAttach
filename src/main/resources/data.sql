INSERT INTO public.vkr_user(
    user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment)
VALUES ('Иванов',
        'Иван',
        'Иванович',
        'artv@bk.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '90419123123',
        'НБФЗОП',
        '596',
        'USER',
        '2018');

INSERT INTO public.vkr_user(
    user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment)
VALUES ('Петров',
        'Петр',
        'Петрович',
        'p@p.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904191234123',
        'НБФЗОП',
        '584',
        'USER',
        '2019');


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
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department)
VALUES ('Злобнопреподов', 'Артем', 'Артемович', 'mail@mail.ru', 'UPV');

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department)
VALUES ('Добропреподов', 'Илья', 'Ильич', 'mail1@mail1.ru', 'UPR');

INSERT INTO public.vkr_order(
    order_theme, order_user, order_lecturer, order_date, order_status, order_accept_date, order_comments)
VALUES ('1', '1', '1', '2022-11-03 15:05:24.000000', 1, '2022-11-03', 'Переделать');

INSERT INTO public.vkr_order(
    order_theme, order_user, order_lecturer, order_date, order_status, order_accept_date, order_comments)
VALUES ('1', '2', null,'2022-11-03 15:05:24.000000', 4, '2022-12-01', 'Принято');