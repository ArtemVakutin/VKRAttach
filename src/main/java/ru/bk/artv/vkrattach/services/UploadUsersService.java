package ru.bk.artv.vkrattach.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.web.dto.UploadAnswer;
import ru.bk.artv.vkrattach.web.dto.UserDto;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;
import ru.bk.artv.vkrattach.services.mappers.UserMapper;
import ru.bk.artv.vkrattach.services.parsers.ExcelParserImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Сервис загрузки тем новых пользователей (SimpleUser) из Excel файла от эиос-а
 */

@Slf4j
@Service
public class UploadUsersService implements UploadDataService<UserDto> {

    private final ExcelParserImpl excelParser;
    private final DataMap dataMap;
    private final UserMapper userMapper;
    private final UserDao userDao;
    private final Validator validator;
    private final UserService userService;

    public UploadUsersService(ExcelParserImpl excelParser, DataMap dataMap, UserMapper userMapper, UserDao userDao, Validator validator, UserService userService) {
        this.excelParser = excelParser;
        this.dataMap = dataMap;
        this.userMapper = userMapper;
        this.userDao = userDao;
        this.validator = validator;
        this.userService = userService;
    }

    /**
     * Парсит Excel файл, добавляет в dataMap список пользователей для загрузки с айдишником по времени.
     *
     * @param file      Excel-файл
     * @param paramsMap Map с параметрами запроса. (специальность, год набора, группа (не обязательно)).
     * @return UploadAnswer со списком пользователей, которые могут быть добавлены и списком пользователей, которые
     * не будут добавлены (и причинами почему не будут добавлены)
     * @throws UploadResourceException если отсутствуют специальность и год набора.
     */
    @Override
    public UploadAnswer<UserDto> checkUploadData(MultipartFile file, Map<String, String> paramsMap) {

        String faculty = Optional.of(paramsMap.get("faculty"))
                .orElseThrow(() -> new UploadResourceException("faculty is not present"));
        String year = Optional.of(paramsMap.get("year"))
                .orElseThrow(() -> new UploadResourceException("year is not present"));
        String group = Optional.of(paramsMap.get("group"))
                .orElse("");


        Map<Integer, List<String>> excelMap = excelParser.parseXls(file);
        List<UserDto> simpleUsers = excelMapToUsersList(excelMap);

        simpleUsers.forEach(user -> {
            user.setFaculty(faculty);
            user.setYear(year);
            user.setGroup(group);
        });

        String id = "";

        List<UserDto> errorAnswerList = simpleUsers.stream()
                .filter(user -> {
                    String validate = validateUser(user);
                    if (userDao.checkLoginExisted(user.getLogin())) {
                        user.setException("Пользователь с таким логином уже зарегистрирован" + "; " + validate);
                        user.setPassword("");
                        return true;
                    } else if (!validate.equals("")) {
                        user.setException(validate);
                        user.setPassword("");
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

        simpleUsers.removeAll(errorAnswerList);

        log.info("In session object is : {}:", simpleUsers.stream().map(UserDto::toString).collect(Collectors.joining(" ")));
        if (!simpleUsers.isEmpty()) {
            id = dataMap.putData(simpleUsers);
        }
        return new UploadAnswer<UserDto>(id, simpleUsers, errorAnswerList);
    }

    /*
     * Валидация пользователей.
     *
     * @param user добавляемый пользователь
     * @return ConstraintViolation, переведенный в String для каждого пользователя. То есть причину, по которой пользователь
     * не может быть загружен
     */
    private String validateUser(UserDto user) {
        Set<ConstraintViolation<UserDto>> validate = validator.validate(user, Default.class);
        if (validate.isEmpty()) {
            return "";
        }
        return validate.stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining("; "));
    }

    /* Метод переводит Map<Integer, List<String>>, полученный из Excel файла в List<UserDto>. При этом данные в UserDto
     * кладутся по ячейкам.
     * @param excelMap полученный map при парсинге из Excel файла.
     * @return
     */
    private List<UserDto> excelMapToUsersList(Map<Integer, List<String>> excelMap) {
        return excelMap.keySet().stream()
                .filter(integer -> !excelMap.get(integer).get(0).equals("username"))
                .map(key -> {
                    List<String> row = excelMap.get(key);
                    UserDto user = new UserDto();
                    String[] s = row.get(3).trim().split(" ");
                    String name = s[0].trim();
                    String patronymic = s.length > 1 ? s[1].trim() : "";

                    user.setRole(Role.USER);
                    user.setLogin(row.get(0));
                    user.setPassword(row.get(1));
                    user.setSurname(row.get(2));
                    user.setName(name);
                    user.setPatronymic(patronymic);
                    return user;
                }).collect(Collectors.toList());
    }


    /**
     * Сохраняет список пользователей в базу данных. Потом удаляет из DataMap
     *
     * @param id айдишник из DataMap
     */
    @Override
    public void registerUploadData(String id) {
        List<UserDto> usersDto = (List<UserDto>) dataMap.get(id);
        usersDto.forEach(user -> {
            log.info("Try to register user : {}", user.toString());
            userService.registerNewUser(user);
            log.info("User registered : {}", user.toString());
        });
        dataMap.remove(id);
    }
}
