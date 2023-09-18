-- Админы

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('ADMIN',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'ADMIN');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('UCH',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'ADMIN');
-- Модераторы

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('UPV',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('UPС',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('CRIMINOLOGY',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('CRIMINALISTICS',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('ORD',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

INSERT INTO public.vkr_user(
    user_login, user_password, user_role)
VALUES ('ADMINISTRATIVE_LAW',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR');

--Юзеры

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
        '581',
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
        'petrov@m.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904123412341',
        'НБФЗОП',
        '581',
        'USER',
        '2018',
        'CAPTAIN',
        'POLICE');

INSERT INTO public.vkr_user(
    user_login, user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment, user_rank, user_rank_type)
VALUES ('SIDOROV',
        'Сидоров',
        'Сидр',
        'Сидорович',
        'sidorov@m.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904123434344',
        'НБФЗОП',
        '582',
        'USER',
        '2018',
        'CAPTAIN',
        'POLICE');


INSERT INTO public.vkr_user(
    user_login, user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment, user_rank, user_rank_type)
VALUES ('ARTEMOV',
        'Артемов',
        'Артем',
        'Артемович',
        'artemov@p.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904191234123',
        'ПФЗОП',
        '581',
        'USER',
        '2018',
        'CAPTAIN',
        'POLICE');

INSERT INTO public.vkr_user(
    user_login, user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment, user_rank, user_rank_type)
VALUES ('VASILIEV',
        'Васильев',
        'Василий',
        'Васильевич',
        'vasiliev@p.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904191234123',
        'ПФЗОП',
        '581',
        'USER',
        '2018',
        'CAPTAIN',
        'POLICE');

INSERT INTO public.vkr_user(
    user_login, user_surname, user_name, user_patronymic, user_email, user_password, user_registration_date, user_telephone, user_faculty, user_group, user_role, user_year_of_recruitment, user_rank, user_rank_type)
VALUES ('POPOV',
        'Попов',
        'Поп',
        'Попович',
        '1@2.ru',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        '2022-11-03 15:05:24.000000',
        '904191234123',
        'ЮВФЗОМ',
        '284',
        'USER',
        '2020',
        'CAPTAIN',
        'POLICE');
