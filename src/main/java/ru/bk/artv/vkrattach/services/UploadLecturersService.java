package ru.bk.artv.vkrattach.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.web.dto.LecturerFullDto;
import ru.bk.artv.vkrattach.web.dto.UploadAnswer;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;
import ru.bk.artv.vkrattach.services.mappers.LecturerMapper;
import ru.bk.artv.vkrattach.services.parsers.ExcelParserImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис загрузки Руководителей ВКР (Lecturer) из Excel файла.
 */
@Slf4j
@Service
public class UploadLecturersService implements UploadDataService<LecturerFullDto> {

    private final ExcelParserImpl excelParser;
    private final LecturerDao lecturerDao;
    private final Validator validator;
    private final LecturerMapper lecturerMapper;
    private final DataMap dataMap;

    public UploadLecturersService(ExcelParserImpl excelParser, LecturerDao lecturerDao, Validator validator, LecturerMapper lecturerMapper, DataMap dataMap) {
        this.excelParser = excelParser;
        this.lecturerDao = lecturerDao;
        this.validator = validator;
        this.lecturerMapper = lecturerMapper;
        this.dataMap = dataMap;
    }

    /**
     * @param file Excel файл
     * @param paramsMap Map параметров запроса, откуда достается кафедра (department)
     * @return UploadAnswer, содержащий списки тех, кто будет загружен и тех, кто не будет загружен, а также айдишник,
     * по которому при подтверждении клиентом будет осуществляться загрузка по поиску из DataMap
     */
    @Override
    public UploadAnswer<LecturerFullDto> checkUploadData(MultipartFile file, Map<String, String> paramsMap) {
        String department = Optional.of(paramsMap.get("department"))
                .orElseThrow(() -> new UploadResourceException("param data is not present"));
        Map<Integer, List<String>> excelMap = excelParser.parseXls(file);
        List<LecturerFullDto> lecturers = excelMapToLecturersList(excelMap, department);
        String id = "";

        List<LecturerFullDto> errorAnswerList = lecturers.stream()
                .filter(lecturer -> {
                    String validate = validateLecturer(lecturer);
                    if (lecturerDao.checkLecturerIsExisted(lecturerMapper.toLecturer(lecturer))) {
                        lecturer.setException("Преподаватель с такими данными (ФИО и кафедра) уже зарегистрирован" + "; " + validate);
                        return true;
                    } else if (!validate.equals("")) {
                        lecturer.setException(validate);
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

        lecturers.removeAll(errorAnswerList);

        if (!lecturers.isEmpty()) {
            id = dataMap.putData(lecturers);
        }
        return new UploadAnswer<LecturerFullDto>(id, lecturers, errorAnswerList);
    }

    //проводит валидацию преподавателей, при непрохождении добавляет текстовую запись ошибки
    private String validateLecturer(LecturerFullDto lecturer) {
        Set<ConstraintViolation<LecturerFullDto>> validate = validator.validate(lecturer, Default.class);
        if (validate.isEmpty()) {
            return "";
        }
        return validate.stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining("; "));
    }

    //переводит Map, полученный из Excel-файла в список Lecturer, устанавливая для каждого department
    private List<LecturerFullDto> excelMapToLecturersList(Map<Integer, List<String>> excelMap, String department) {
        return excelMap.keySet().stream()
                .filter(integer -> !excelMap.get(integer).get(0).equalsIgnoreCase("ФАМИЛИЯ"))
                .map(key -> {
                    List<String> row = excelMap.get(key);
                    LecturerFullDto lecturer = new LecturerFullDto();
                    String[] s = row.get(1).trim().split(" ");
                    String name = s[0].trim();
                    String patronymic = s.length > 1 ? s[1].trim() : "";

                    lecturer.setSurname(row.get(0));
                    lecturer.setName(name);
                    lecturer.setPatronymic(patronymic);
                    lecturer.setDepartment(department);

                    return lecturer;
                }).collect(Collectors.toList());
    }

    /**
     * Сохраняет ранее список Lecturer из Excel файла в базу. Список берется из dataMap
     *
     * @param id ранее отправлялся клиенту.
     */
    @Override
    public void registerUploadData(String id) {
        var lecturers = (List<LecturerFullDto>)dataMap.get(id);
        lecturers.stream().map(lecturerMapper::toLecturer)
                .forEach(lecturerDao::saveLecturer);
        dataMap.remove(id);
    }
}
