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
        '2008');

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
        '2009');


INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('доение коров в особых условиях', 'UPV', 'НБФЗОП');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('поедание кобыл на завтрак', 'UPV', 'НБФЗОП');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('прыжки в воду с самолета без парашюта', 'UPR', 'НБФЗОП');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('кидание апельсинов на скорость', 'UPR', 'НБФЗОП');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('доение коров в особых условиях', 'UPV', 'ЮВФЗОМ');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('поедание кобыл на завтрак', 'UPV', 'ЮВФЗОМ');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('прыжки в воду с самолета без парашюта', 'UPR', 'ЮВФЗОМ');

INSERT INTO public.vkr_theme(
    theme_name, theme_department, theme_faculty)
VALUES ('кидание апельсинов на скорость', 'UPR', 'ЮВФЗОМ');

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department)
VALUES ('Злобнопреподов', 'Артем', 'Артемович', 'mail@mail.ru', 'UPV');

INSERT INTO public.vkr_lecturer(
    lecturer_name, lecturer_surname, lecturer_patronymic, lecturer_email, lecturer_department)
VALUES ('Добропреподов', 'Илья', 'Ильич', 'mail1@mail1.ru', 'UPR');

INSERT INTO public.vkr_order(
    order_theme, order_preferred_lecturer, order_user, order_lecturer, order_date, order_accept, order_accept_date)
VALUES ('1', '1', '1', '1', '2022-11-03 15:05:24.000000', 'true', '2022-11-03 15:05:24.000000');

INSERT INTO public.vkr_order(
    order_theme, order_preferred_lecturer, order_user, order_lecturer, order_date, order_accept, order_accept_date)
VALUES ('1', null, '2', null,'2022-11-03 15:05:24.000000', 'false', '2022-11-03 15:05:24.000000');