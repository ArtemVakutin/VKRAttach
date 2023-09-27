-- кафедры (type = DEPARTMENT)
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('DEPARTMENT', 'УПв', 'уголовного права'),
       ('DEPARTMENT', 'УПц', 'уголовного процесса'),
       ('DEPARTMENT', 'Криминологии', 'криминологии'),
       ('DEPARTMENT', 'Криминалистики', 'криминалистики'),
       ('DEPARTMENT', 'ОРД', 'оперативно-розыскной деятельности органов внутренних дел'),
       ('DEPARTMENT', 'АПиАД', 'административного права и административной деятельности органов внутренних дел');

--факультеты (type = FACULTY)
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('FACULTY', 'НБФЗОП', 'специальность 40.05.01 Правовое обеспечение национальной безопасности'),
       ('FACULTY', 'ПФЗОП', 'специальность 40.05.02 Правоохранительная деятельность'),
       ('FACULTY', 'ЮВФЗОМ', 'направление подготовки 40.04.01 Юриспруденция');

--год набора (type = YEAR)
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('YEAR', '2019', '2019'),
       ('YEAR', '2020', '2020');

--звание (type = RANK)
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('RANK', 'отсутствует', 'отсутствует'),
       ('RANK', 'рядовой', 'рядовой'),
       ('RANK', 'младший сержант', 'младший сержант'),
       ('RANK', 'сержант', 'сержант'),
       ('RANK', 'старший сержант', 'старший сержант'),
       ('RANK', 'старшина', 'старшина'),
       ('RANK', 'прапорщик', 'прапорщик'),
       ('RANK', 'старший прапорщик', 'старший прапорщик'),
       ('RANK', 'младший лейтенант', 'младший лейтенант'),
       ('RANK', 'лейтенант', 'лейтенант'),
       ('RANK', 'старший лейтенант', 'старший лейтенант'),
       ('RANK', 'капитан', 'капитан'),
       ('RANK', 'майор', 'капитан'),
       ('RANK', 'подполковник', 'подполковник'),
       ('RANK', 'полковник', 'полковник'),
       ('RANK', 'генерал-майор', 'генерал-майор');

--звание преподавателя (type = LECTURER_RANK) Сделано для уменьшения количества званий в выпадающем списке
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('LECTURER_RANK', 'отсутствует', 'отсутствует'),
       ('LECTURER_RANK', 'младший лейтенант', 'младший лейтенант'),
       ('LECTURER_RANK', 'лейтенант', 'лейтенант'),
       ('LECTURER_RANK', 'старший лейтенант', 'старший лейтенант'),
       ('LECTURER_RANK', 'капитан', 'капитан'),
       ('LECTURER_RANK', 'майор', 'капитан'),
       ('LECTURER_RANK', 'подполковник', 'подполковник'),
       ('LECTURER_RANK', 'полковник', 'полковник'),
       ('LECTURER_RANK', 'генерал-майор', 'генерал-майор'),
       ('LECTURER_RANK', 'генерал-лейтенант', 'генерал-лейтенант');

--вид звания (type = RANK_type) Вид звания (у преподавателей по умолчанию "полиции")
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('RANK_TYPE', 'полиции', 'полиции'),
       ('RANK_TYPE', 'юстиции', 'юстиции'),
       ('RANK_TYPE', 'внутренней службы', 'внутренней службы'),
       ('RANK_TYPE', 'отсутствует', 'отсутствует');

--научная степень (type = ACADEMIC_DEGREE) Научная степень
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('ACADEMIC_DEGREE', 'отсутствует', ''),
       ('ACADEMIC_DEGREE', 'к.ю.н.', 'кандидат юридических наук'),
       ('ACADEMIC_DEGREE', 'д.ю.н.', 'доктор юридических наук');

--ученое звание (type = ACADEMIC_TITLE) Ученое звание
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('ACADEMIC_TITLE', 'отсутствует', 'отсутствует'),
       ('ACADEMIC_TITLE', 'доцент', 'доцент'),
       ('ACADEMIC_TITLE', 'профессор', 'профессор');

--должность научного руководителя (type = LECTURER_POSITION)
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('LECTURER_POSITION', 'преподаватель', 'преподаватель'),
       ('LECTURER_POSITION', 'старший преподаватель', 'старший преподаватель'),
       ('LECTURER_POSITION', 'доцент', 'доцент'),
       ('LECTURER_POSITION', 'профессор', 'профессор'),
       ('LECTURER_POSITION', 'заместитель начальника кафедры', 'заместитель начальника кафедры'),
       ('LECTURER_POSITION', 'начальник кафедры', 'начальник кафедры');


--должность обучающегося (для заочки - слушатель) (type = USER_POSITION)
INSERT INTO public.vkr_config_data(config_type, config_value, config_label)
VALUES ('USER_POSITION', 'слушатель', 'слушатель'),
       ('USER_POSITION', 'курсант', 'курсант'),
       ('USER_POSITION', 'командир отделения', 'командир отделения'),
       ('USER_POSITION', 'заместитель командира взвода', 'заместитель командира взвода'),
       ('USER_POSITION', 'заместитель начальника кафедры', 'командир взвода');





