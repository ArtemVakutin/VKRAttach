package ru.bk.artv.vkrattach.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.LogsDownloadService;
import ru.bk.artv.vkrattach.web.dto.LogFileNameTime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Контроллер для скачивания логов админом. Увы, доступа к серверу, на котором развернут проект, может и не быть,
 * а отдельно ставить сервер с эластиком никто не даст.
 */
@Slf4j
@RestController
@RequestMapping(path = "rest/data/logs")
public class LogsController {

    LogsDownloadService logsDownloadService;

    public LogsController(LogsDownloadService logsDownloadService) {
        this.logsDownloadService = logsDownloadService;
    }

    /**
     * Контроллер для скачивания файлов логов
     *
     * @param fileName имя файла логов
     * @return поток с файлом
     */
    @GetMapping(path = "/get",
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<InputStreamResource> downloadLog(@RequestParam String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=" + fileName);

        try (ByteArrayInputStream bis = logsDownloadService.downloadLog(fileName);) {
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(bis));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Не удалось выгрузить файл логов", e);
        }
    }

    /**
     * Получение перечня файлов логов в формате имя-дата
     *
     * @return перечень лог-файлов
     */
    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public List<LogFileNameTime> getLogsList() {
        return logsDownloadService.getLogsList();
    }

    /**
     * Получение перечня файлов логов в формате имя-дата
     *
     * @return перечень лог-файлов
     */
    @DeleteMapping
    @Secured({"ROLE_ADMIN"})
    public void deleteLog(@RequestParam String fileName) {
        logsDownloadService.deleteLog(fileName);
    }
}
