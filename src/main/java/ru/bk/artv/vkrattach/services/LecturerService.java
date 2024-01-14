package ru.bk.artv.vkrattach.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.services.model.Lecturer;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.web.dto.LecturerDto;
import ru.bk.artv.vkrattach.web.dto.LecturerFullDto;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.mappers.LecturerMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CRUD для Руководителей ВКР (Lecturer) плюс получение списков руководителей.
 */
@Service
public class LecturerService {

    private final LecturerMapper lecturerMapper;
    private final LecturerDao lecturerDao;
    private final OrderDao orderDao;

    public LecturerService(LecturerMapper lecturerMapper, LecturerDao lecturerDao, OrderDao orderDao) {
        this.lecturerMapper = lecturerMapper;
        this.lecturerDao = lecturerDao;
        this.orderDao = orderDao;
    }

    /**
     * @param lecturerFullDto Dto руководителя ВКР со всеми полями
     * @return lecturerFullDto уже отредактированный
     */
    public LecturerFullDto patchLecturer(LecturerFullDto lecturerFullDto) {
        Lecturer lecturer = lecturerDao.getLecturerById(lecturerFullDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Научный руководитель с ID : "
                        + lecturerFullDto.getId() + "не найден"));

        lecturerMapper.toLecturer(lecturerFullDto, lecturer);
        lecturerDao.patchLecturer(lecturer);
        return lecturerMapper.toLecturerFullDTO(lecturer);
    }

    /**
     * @param lecturerFullDto Dto руководителя ВКР со всеми полями
     * @return сохраненный lecturerFullDto
     */
    public LecturerFullDto putLecturer(LecturerFullDto lecturerFullDto) {
        Lecturer lecturer = new Lecturer();
        lecturerMapper.toLecturer(lecturerFullDto, lecturer);
        lecturerDao.patchLecturer(lecturer);
        return lecturerMapper.toLecturerFullDTO(lecturer);
    }

    /**
     * @param id айдишник Lecturer
     */
    public void deleteLecturer(Long id) {
        Lecturer lecturer = lecturerDao.getLecturerById(id).orElseThrow(() -> new ResourceNotFoundException("Lecturer with id : " + id + "are not found"));
        lecturerDao.deleteLecturer(lecturer);
    }

    /**
     * @param department кафедра
     * @return возвращает Dto список руководителей ВКР для пользователя без расширенного списка данных
     */
    public List<LecturerDto> getSimpleLecturers(String department) {
        List<Lecturer> lecturers = lecturerDao.getLecturers(department);
        return lecturers.stream().map(lecturer -> lecturerMapper.toLecturerDTO(lecturer)).toList();
    }


    /**
     * @param department кафедра
     * @param faculty    специальность/направление подготовки
     * @param year       год набора
     * @return список LecturerFullDto со всеми данными и подсчитанными заявками по факультетам и годам набора.
     */
    @Transactional
    public List<LecturerDto> getFullLecturers(String department, String faculty, String year) {
        List<Lecturer> lecturers = lecturerDao.getLecturers(department);
        if (faculty != null && department != null && year != null
                && !year.equals("") && !faculty.equals("") && !department.equals("")) {
            return lecturers.stream().map(lecturer -> {
                LecturerFullDto lecturerDTO = lecturerMapper.toLecturerFullDTO(lecturer);
                var size = orderDao.getOrders(lecturer).stream().filter(order -> {
                    var orderDep = order.getTheme().getDepartment();
                    var orderYear = order.getTheme().getYear();
                    return (orderDep.equals(department) && orderYear.equals(year));
                }).toList().size();
                lecturerDTO.setOrdersCount(size);
                return lecturerDTO;
            }).collect(Collectors.toList());
        }
        return lecturers.stream().map(lecturer -> lecturerMapper.toLecturerFullDTO(lecturer)).collect(Collectors.toList());
    }
}
