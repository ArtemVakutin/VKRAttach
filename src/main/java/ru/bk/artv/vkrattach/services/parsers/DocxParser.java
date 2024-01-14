package ru.bk.artv.vkrattach.services.parsers;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocxParser {
    List<String> parseDocx(MultipartFile file);
}
