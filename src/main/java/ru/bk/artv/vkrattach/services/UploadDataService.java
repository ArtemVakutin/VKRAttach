package ru.bk.artv.vkrattach.services;

import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.web.dto.UploadAnswer;

import java.util.Collection;
import java.util.Map;


/**
 * Интерфейс сервиса загрузки данных. Предполагается, что на первом этапе клиент отправляет на проверку и парсинг
 * файл с данными, после чего пользователь подтверждает сохранение (либо исправляет ошибки). В UploadDataService
 * добавляется список загружаемых данных, список данных, которые не загрузятся с указанием причин (опционально) и
 * айдишник для загрузки.
 *
 * @param <T> - непосредственно Dto объект, список которых  загружается из файла (Excel или Word)
 */
public interface UploadDataService<T> {

    UploadAnswer<T> checkUploadData(MultipartFile file, Map<String, String> paramsMap);

    void registerUploadData(String id);

}
