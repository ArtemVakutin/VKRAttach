package ru.bk.artv.vkrattach.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.dto.LecturerDTO;
import ru.bk.artv.vkrattach.dto.ThemeDTO;
import ru.bk.artv.vkrattach.dto.UploadAnswer;
import ru.bk.artv.vkrattach.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;
import ru.bk.artv.vkrattach.services.DownloadService;
import ru.bk.artv.vkrattach.services.UploadService;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(path = "rest/data/download")
public class DownloadRestController {

    DownloadService DownloadService;

    @GetMapping(path = "/attached",
            produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    public ResponseEntity<InputStreamResource> downloadAttachedOrdersDocx(@RequestParam("department") String department,
                                                            @RequestParam("faculty") String faculty,
                                                            @RequestParam("year") String year) {
        ByteArrayInputStream bis = DownloadService.downloadAttachedOrders(department, faculty, year);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=orders.docx");
        return ResponseEntity.ok().headers(headers).
                body(new InputStreamResource(bis));
    }


        return themesService.deleteThemes(department, faculty, year);
    }

}
