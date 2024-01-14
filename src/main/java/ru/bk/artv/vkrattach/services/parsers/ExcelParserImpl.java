package ru.bk.artv.vkrattach.services.parsers;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Класс для парсинга Excel файлов.
 */
@Slf4j
@Service
public class ExcelParserImpl implements ExcelParser {


    /**
     * Парсит excel-файл в Map.
     *
     * @param file excel-файл
     * @return Map, где Integer - это номер строки в таблице, а List<String> - коллекция, содержащая ячейки строки таблицы
     */
    @Override
    public Map<Integer, List<String>> parseXls(MultipartFile file) {
        Map<Integer, List<String>> excelMap = new HashMap<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workBook = WorkbookFactory.create(is);
            parseWorkBook(excelMap, workBook);
        } catch (IOException | IllegalArgumentException | EncryptedDocumentException e) {
            e.printStackTrace();
            throw new UploadResourceException(e.getMessage(), e);
        }
        log.debug("Excel file : {} parsed : \n {}", file.getOriginalFilename(), mapAsString(excelMap));
        return excelMap;
    }

    //Парсит таблицу построчно
    private void parseWorkBook(Map<Integer, List<String>> excelMap, Workbook workBook) {
        Sheet sheet = workBook.getSheetAt(0);

        int firstRow = sheet.getFirstRowNum();
        int lastRow = sheet.getLastRowNum();

        for (int index = firstRow; index <= lastRow; index++) {
            List<String> rowList = new ArrayList<>();
            Row row = sheet.getRow(index);

            for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
                Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                rowList.add(parseCell(cell));
            }
            excelMap.put(index, rowList);
        }
    }

    //Возвращает содержимое ячейки в String вне зависимости от типа ячейки
    private String parseCell(Cell cell) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();

        if (cellType.equals(CellType.STRING)) {
            return cell.getStringCellValue();
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            } else {
                return String.valueOf((int) cell.getNumericCellValue());
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            return String.valueOf(cell.getBooleanCellValue());
        }
        if (cellType.equals(CellType.ERROR)) {
            return String.valueOf(cell.getErrorCellValue());
        }
        return "";
    }

    //для логирования, переводит Map в String
    private String mapAsString(Map<Integer, List<String>> excelMap) {
        return excelMap.keySet().stream()
                .map(key -> key + "=" + excelMap.get(key)
                        .stream()
                        .collect(Collectors.joining("-", "{", "}")))
                .collect(Collectors.joining(",\n ", "[", "]"));
    }
}
