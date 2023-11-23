package ru.bk.artv.vkrattach.services;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.domain.user.Role;
import ru.bk.artv.vkrattach.dto.LecturerFullDto;
import ru.bk.artv.vkrattach.dto.ThemeDTO;
import ru.bk.artv.vkrattach.dto.UploadAnswer;
import ru.bk.artv.vkrattach.dto.UserDTO;
import ru.bk.artv.vkrattach.exceptions.UploadResourceException;
import ru.bk.artv.vkrattach.services.mappers.LecturerMapper;
import ru.bk.artv.vkrattach.services.mappers.ThemesMapper;
import ru.bk.artv.vkrattach.services.mappers.UserMapper;
import ru.bk.artv.vkrattach.services.parsers.DocxParser;
import ru.bk.artv.vkrattach.services.parsers.ExcelParser;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UploadService {
    ExcelParser excelParser;
    DocxParser docxParser;
    UserDao userDao;
    UserService userService;
    LecturerDao lecturerDao;
    ThemesDao themesDao;
    PasswordEncoder passwordEncoder;
    Validator validator;
    UserMapper userMapper;
    LecturerMapper lecturerMapper;
    ThemesMapper themesMapper;

    public UploadAnswer<UserDTO> checkUploadUsers(MultipartFile file, HttpSession session, String faculty, String year, String group) {
        Map<Integer, List<String>> excelMap = excelParser.parseXls(file);
        List<UserDTO> simpleUsers = excelMapToUsersList(excelMap);

        simpleUsers.forEach(user -> {
            user.setFaculty(faculty);
            user.setYear(year);
            user.setGroup(group);
        });

        String id = "";

        List<UserDTO> errorAnswerList = simpleUsers.stream()
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

        log.info("In session object is : {}:", simpleUsers.stream().map(UserDTO::toString).collect(Collectors.joining(" ")));
        if (!simpleUsers.isEmpty()) {
            id = ((long) (Math.random() * 10000000)) + "";
            session.setAttribute(id, simpleUsers);
        }

        return new UploadAnswer<UserDTO>(id, simpleUsers, errorAnswerList);
    }

    private String validateUser(UserDTO user) {
        Set<ConstraintViolation<UserDTO>> validate = validator.validate(user, Default.class);
        if (validate.isEmpty()) {
            return "";
        }
        return validate.stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining("; "));
    }

    private List<UserDTO> excelMapToUsersList(Map<Integer, List<String>> excelMap) {
        return excelMap.keySet().stream()
                .filter(integer -> !excelMap.get(integer).get(0).equals("username"))
                .map(key -> {
                    List<String> row = excelMap.get(key);
                    UserDTO user = new UserDTO();
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

    public void registerUploadUsers(List<UserDTO> usersDto) {
        usersDto.stream().forEach(user -> {
            log.info("Try to register user : {}", user.toString());
            userService.registerNewUser(user);
            log.info("User registered : {}", user.toString());
        });
    }

    public UploadAnswer<ThemeDTO> checkUploadThemes(MultipartFile file, HttpSession session, String department,
                                                    String faculty, String year) {
        String id = "";

        List<String> docxMap = docxParser.parseDocx(file);
        List<ThemeDTO> themes = docxMap.stream().map(s -> {
            ThemeDTO theme = new ThemeDTO();
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

        id = ((long) (Math.random() * 10000000)) + "";
        session.setAttribute(id, themes);

        return new UploadAnswer<ThemeDTO>(id, themes, new ArrayList<>());
    }

    public UploadAnswer<LecturerFullDto> checkUploadLecturers(MultipartFile file, HttpSession session, String department) {
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

        log.info("In session object is : {}:", lecturers.stream().map(LecturerFullDto::toString).collect(Collectors.joining(" ")));
        if (!lecturers.isEmpty()) {
            id = ((long) (Math.random() * 10000000)) + "";
            session.setAttribute(id, lecturers);
        }

        return new UploadAnswer<LecturerFullDto>(id, lecturers, errorAnswerList);
    }

    private String validateLecturer(LecturerFullDto lecturer) {
        Set<ConstraintViolation<LecturerFullDto>> validate = validator.validate(lecturer, Default.class);
        if (validate.isEmpty()) {
            return "";
        }
        return validate.stream().map(ConstraintViolation::getMessageTemplate).collect(Collectors.joining("; "));
    }

    private List<LecturerFullDto> excelMapToLecturersList(Map<Integer, List<String>> excelMap, String department) {
        return excelMap.keySet().stream()
                .filter(integer -> !excelMap.get(integer).get(0).toUpperCase().equals("ФАМИЛИЯ"))
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

    public void registerUploadLecturers(List<LecturerFullDto> lecturers) {
        lecturers.stream().map(lecturer -> lecturerMapper.toLecturer(lecturer))
                .forEach(lecturer -> lecturerDao.saveLecturer(lecturer));
    }

    public void registerUploadThemes(List<ThemeDTO> themes) {
        themes.stream().map(themeDTO -> themesMapper.toTheme(themeDTO)).forEach(theme -> themesDao.addTheme(theme));
    }
}
