package ru.bk.artv.vkrattach;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 10.12.2023 Сделать человеческую авторизацию и свитчи! +++ (конечно TokenUser -> DefaultUser не очень, но поправим позднее)
// TODO: 11.12.2023 Добавить возможность генерации рапорта ++++ (через жопу но пашет)
// TODO: 11.12.2023 Сделать отлов ошибок в морде +++
// TODO: 11.12.2023 Добавить для Dev режима H2O базу данных +++ (зачем хз, но работает, лучше впихнуть постгрес в образ)
// TODO: 11.12.2023 Присобачить морду на выдачу +++
// TODO: 11.12.2023 И Юнит тесты!

// TODO: 26.12.2023 При добавлении заявки ручками через форму надо дать возможность сразу выбирать научного
// TODO: 26.12.2023 При добавлении заявки ручками через форму надо запретить устанавливать левых руководителей
// TODO: 26.12.2023 Перезагрузка списка научных после добавления заявки с количеством добавленных
// TODO: 26.12.2023 Из таблицы модератора по заявкам можно одобрить более одной заявки 
@SpringBootApplication
public class VkrAttachApplication {
    public static void main(String[] args) {
        SpringApplication.run(VkrAttachApplication.class, args);
    }
}
