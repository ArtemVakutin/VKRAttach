package ru.bk.artv.vkrattach.services.parsers;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ExcelParser {
    Map<Integer, List<String>> parseXls(MultipartFile file);
}
