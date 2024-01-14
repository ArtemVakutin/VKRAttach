-- Админы

INSERT INTO vkr_user(user_login, user_password, user_role)
VALUES ('ADMIN',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'ADMIN'),
       ('UCH',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'ADMIN');

-- Модераторы
INSERT INTO vkr_user(user_login, user_password, user_role, user_department)
VALUES ('UPV',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR', 'УПв'),
       ('UPС',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR', 'УПц'),
       ('CRIMINOLOGY',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR', 'Криминологии'),
       ('CRIMINALISTICS',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR', 'Криминалистики'),
       ('ORD',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR', 'ОРД'),
       ('ADMINISTRATIVE_LAW',
        '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C',
        'MODERATOR', 'АПиАД');

--Юзеры

INSERT INTO vkr_user (
    user_login, user_surname, user_name, user_patronymic, user_email,
    user_password, user_telephone, user_faculty, user_group, user_role,
    user_year, user_rank, user_rank_type, user_position
) VALUES
      ('user1', 'Иванов', 'Иван', 'Иванович', 'ivanov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '591', 'USER', '2019', 'старший лейтенант', 'полиции', 'слушатель'),
      ('user2', 'Петров', 'Петр', 'Петрович', 'petrov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ПФЗОП', '502', 'USER', '2020', 'лейтенант', 'полиции', 'слушатель'),
      ('user3', 'Сидоров', 'Сидор', 'Сидорович', 'sidorov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ЮВФЗОМ', '593', 'USER', '2019', 'капитан', 'юстиции', 'слушатель'),
      ('user4', 'Кузнецов', 'Алексей', 'Алексеевич', 'kuznetsov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '501', 'USER', '2020', 'полковник', 'внутренней службы', 'слушатель'),
      ('user5', 'Смирнова', 'Ольга', 'Анатольевна', 'smirnova@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ПФЗОП', '592', 'USER', '2019', 'генерал-лейтенант', 'полиции', 'слушатель'),
      ('user6', 'Козлов', 'Игорь', 'Игоревич', 'kozlov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ЮВФЗОМ', '503', 'USER', '2020', 'генерал-майор', 'полиции', 'слушатель'),
      ('user7', 'Новикова', 'Елена', 'Петровна', 'novikova@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '591', 'USER', '2019', 'младший лейтенант', 'внутренней службы', 'слушатель'),
      ('user8', 'Васнецов', 'Сергей', 'Иванович', 'vasnetsov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ПФЗОП', '502', 'USER', '2020', 'прапорщик', 'юстиции', 'слушатель'),
      ('user9', 'Григорьев', 'Дмитрий', 'Дмитриевич', 'grigoriev@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ЮВФЗОМ', '593', 'USER', '2019', 'отсутствует', 'внутренней службы', 'слушатель'),
      ('user10', 'Богданов', 'Николай', 'Александрович', 'bogdanov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '501', 'USER', '2020', 'младший лейтенант', 'юстиции', 'слушатель'),
      ('user11', 'Краснов', 'Сергей', 'Сергеевич', 'krasnov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ПФЗОП', '592', 'USER', '2019', 'генерал-лейтенант', 'внутренней службы', 'слушатель'),
      ('user12', 'Трофимов', 'Максим', 'Игоревич', 'trofimov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ЮВФЗОМ', '503', 'USER', '2020', 'генерал-майор', 'полиции', 'слушатель'),
      ('user13', 'Осипова', 'Татьяна', 'Владимировна', 'osipova@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '591', 'USER', '2019', 'капитан', 'юстиции', 'слушатель'),
      ('user14', 'Артемьев', 'Денис', 'Денисович', 'artemev@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ПФЗОП', '502', 'USER', '2020', 'старший лейтенант', 'полиции', 'слушатель'),
      ('user15', 'Кудрявцев', 'Игорь', 'Владимирович', 'kudryavtsev@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ЮВФЗОМ', '593', 'USER', '2019', 'генерал-майор', 'внутренней службы', 'слушатель'),
      ('user16', 'Герасимов', 'Александр', 'Александрович', 'gerasimov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '501', 'USER', '2020', 'подполковник', 'полиции', 'слушатель'),
      ('user17', 'Савельева', 'Мария', 'Андреевна', 'savelieva@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ПФЗОП', '592', 'USER', '2019', 'генерал-лейтенант', 'внутренней службы', 'слушатель'),
      ('user18', 'Романов', 'Михаил', 'Михайлович', 'romanov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ЮВФЗОМ', '503', 'USER', '2020', 'генерал-майор', 'полиции', 'слушатель'),
      ('user19', 'Денисов', 'Владимир', 'Владимирович', 'denisov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '591', 'USER', '2019', 'подполковник', 'юстиции', 'слушатель'),
      ('user20', 'Тимофеев', 'Артем', 'Артемович', 'timofeev@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ПФЗОП', '502', 'USER', '2020', 'прапорщик', 'внутренней службы', 'слушатель'),
      ('user21', 'Беляев', 'Егор', 'Егорович', 'belyaev@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'ЮВФЗОМ', '593', 'USER', '2019', 'генерал-лейтенант', 'полиции', 'слушатель'),
      ('user22', 'Мельников', 'Антон', 'Антонович', 'melnikov@example.com', '$2a$10$D1i4amR0a3t43xwE8SDseup26fV9Yf9ZBs9bdG8.vlu8gqRFEHd1C', '1234567890', 'НБФЗОП', '501', 'USER', '2020', 'младший лейтенант', 'внутренней службы', 'слушатель');