package ru.bk.artv.vkrattach.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.web.dto.ThemeDto;
import ru.bk.artv.vkrattach.web.dto.UploadAnswer;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;
import ru.bk.artv.vkrattach.services.mappers.ThemesMapper;
import ru.bk.artv.vkrattach.services.parsers.DocxParserImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис загрузки тем ВКР (Theme) из Docx файла.
 */

@Service
public class UploadThemesService implements UploadDataService<ThemeDto> {

    private final DocxParserImpl docxParser;
    private final DataMap dataMap;
    private final ThemesMapper themesMapper;
    private final ThemesDao themesDao;

    public UploadThemesService(DocxParserImpl docxParser, DataMap dataMap, ThemesMapper themesMapper, ThemesDao themesDao) {
        this.docxParser = docxParser;
        this.dataMap = dataMap;
        this.themesMapper = themesMapper;
        this.themesDao = themesDao;
    }

    /**
     * Добавляет из Docx файла в DataMap List<ThemeDTO> для последующей загрузки пользователем при подтверждении. Айдишник
     * дается по времени запроса.
     *
     * @param file Docx файл, в котором темы ВКР отделены друг от друга знаками абзацев
     * @param paramsMap Map с параметрами, пришедшими из запроса (department, faculty, year), которые потом подставляются
     *                  в темы
     * @return UploadAnswer с темами, которые загружаются. Незагруженных тем в ответ не вставляется!
     * @throws UploadResourceException в случае, если для конкретного года набора, кафедры
     * и специальности имеются какие либо темы ВКР.
     */
    @Override
    public UploadAnswer<ThemeDto> checkUploadData(MultipartFile file, Map<String, String> paramsMap) {
        String department = Optional.of(paramsMap.get("department"))
                .orElseThrow(() -> new UploadResourceException("department is not present"));
        String faculty = Optional.of(paramsMap.get("faculty"))
                .orElseThrow(() -> new UploadResourceException("faculty is not present"));
        String year = Optional.of(paramsMap.get("year"))
                .orElseThrow(() -> new UploadResourceException("year is not present"));
        String id = "";

        List<String> docxMap = docxParser.parseDocx(file);
        List<ThemeDto> themes = docxMap.stream().map(s -> {
            ThemeDto theme = new ThemeDto();
            theme.setThemeName(s);
            theme.setDepartment(department);
            theme.setFaculty(faculty);
            theme.setYear(year);
            return theme;
        }).collect(Collectors.toList());
        if (!themesDao.getThemesByDepartmentFacultyYearNoException(department, faculty, year).isEmpty()){
            throw new UploadResourceException("Темы для кафедры: " + department + ", факультета " + faculty + " "
                    + year + "года набора уже загружены, удалите их перед новой загрузкой");
        }

        id = dataMap.putData(themes);

        return new UploadAnswer<ThemeDto>(id, themes, new ArrayList<>());
    }

    /**
     * Сохраняет темы в базе данных по айдишнику.
     *
     * @param id айдишник DataMap-а
     */
    @Override
    public void registerUploadData(String id) {
        List<ThemeDto> themes = (List<ThemeDto>) dataMap.get(id);
        themes.stream().map(themesMapper::toTheme).forEach(themesDao::addTheme);
        dataMap.remove(id);
    }
}
