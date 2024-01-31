package ru.bk.artv.vkrattach.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.web.dto.LogFileNameTime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

/**
 * Необходим для скачивания логов администратором при отсутствии доступа к серверу как таковому.
 */
@Service
public class LogsDownloadService {


    @Value("${logging.file.path}")
    String logsPath;

    /**
     * @param fileName имя файла (не полный путь)
     * @return ByteArrayInputStream
     */
    public ByteArrayInputStream downloadLog(String fileName) {
        String path = logsPath + "/" + fileName;
        try(InputStream inputStream = Files.newInputStream(Paths.get(path))) {

            return new ByteArrayInputStream(inputStream.readAllBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаление лишних файлов логов администраторов. При попытке удаления текущего файла логов выбрасывается ошибка.
     *
     * @param fileName имя удаляемого в папке логов файла
     */
    public void deleteLog(String fileName) {
        String path = logsPath + "/" + fileName;
        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("Невозможно удалить файл");
        }
    }

    /**
     * Получает перечень файлов, содержащихся в каталоге логирования.
     *
     * @return List<LogFileNameTime> где 1 значение - имя файла, второе - время создания
     */
    public List<LogFileNameTime> getLogsList() {

        try(Stream<Path> paths = Files.list(Paths.get(logsPath));) {
            return paths.map(path -> {
                String fileName = path.getFileName().toString();
                Instant fileCreationDate = null;

                try {
                    fileCreationDate = Files.readAttributes(path, BasicFileAttributes.class)
                            .creationTime()
                            .toInstant();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String time = LocalDateTime.ofInstant(fileCreationDate, ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm:ss"));

                return new LogFileNameTime(fileName, time);
            }).toList();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
