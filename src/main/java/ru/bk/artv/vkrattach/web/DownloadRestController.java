package ru.bk.artv.vkrattach.web;


import com.turkraft.springfilter.boot.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.DownloadService;
import ru.bk.artv.vkrattach.services.utils.MapToStringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "rest/data/download")
public class DownloadRestController {

    DownloadService DownloadService;

    @GetMapping(path = "/orders",
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<InputStreamResource> downloadAttachedOrdersDocx(@Filter Specification<Order> spec, HttpServletRequest http) {
        log.info("Request params : {}", MapToStringUtil.mapAsString(http.getParameterMap()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=orders.docx");

        try (ByteArrayInputStream bis = DownloadService.downloadAttachedOrders(spec);) {

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(bis));
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceNotFoundException("Таких заявок не обнаружено", e);
        }
    }
}
