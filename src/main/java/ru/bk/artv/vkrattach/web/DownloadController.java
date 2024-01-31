package ru.bk.artv.vkrattach.web;


import com.turkraft.springfilter.boot.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.services.TokenUserToDefaultUserConverter;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.DownloadService;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.utils.MapToStringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * REST-сервис для загрузки генерируемых Docx файлов, а также образцов документов
 */
@Slf4j
@RestController
@RequestMapping(path = "rest/data/download")
public class DownloadController {

    //сервис для загрузки docx файлов с данными
    private final DownloadService downloadService;
    private final TokenUserToDefaultUserConverter converter;

    public DownloadController(ru.bk.artv.vkrattach.services.DownloadService downloadService, TokenUserToDefaultUserConverter converter) {
        this.downloadService = downloadService;
        this.converter = converter;
    }

    /**
     * Получение docx списка зарегистрированных выпускных квалификационных работ в формате для вставки в приказ.
     * Корректно выдается только при условии заполнения всех данных пользователей.
     *
     * @param spec спецификации, по которым сервис вставляет данные в список
     * @param http request для логирования
     * @return docx файл для загрузки
     */
    @GetMapping(path = "/orders",
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<InputStreamResource> downloadAttachedOrdersDocx(@Filter Specification<Order> spec,
                                                                          HttpServletRequest http) {
        log.info("Request params : {}", MapToStringUtil.mapAsString(http.getParameterMap()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=orders.docx");

        try (ByteArrayInputStream bis = downloadService.downloadAttachedOrders(spec);) {

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(bis));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Таких заявок не обнаружено", e);
        }
    }

    /**
     * Сервис загрузки сгенерированного рапорта на заявку на закрепление ВКР
     *
     * @param id айди пользователя, рапорт которого надо загружать. Для модератора и админа.
     * @param tokenUser аутентифицированный пользователь
     * @return поток с рапортом
     */
    @GetMapping(path = "/docs",
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InputStreamResource> downloadReport(@RequestParam(value = "id", required = false) Long id,
                                                                          @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=orders.docx");

        id = switch (user.getRole()) {
            case USER -> user.getId();
            case ADMIN, MODERATOR -> id;
        };

        try (ByteArrayInputStream bis = downloadService.downloadReport(id);) {

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(bis));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Не удалось сгенерировать документ", e);
        }
    }

    /**
     * Сервис загрузки образцов загружаемых документов. Позже при желании можно переделать в сервис загрузки любых образцов
     *
     * @param fileName название файла, должно соответствовать загружаемому файлу из папки files
     * @return поток с файлом
     */
    @GetMapping(path = "/example",
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public ResponseEntity<InputStreamResource> downloadExample(@RequestParam String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=" + fileName);

        try (ByteArrayInputStream bis = downloadService.downloadExample(fileName);) {
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(bis));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Не удалось выгрузить документ", e);
        }
    }
}
