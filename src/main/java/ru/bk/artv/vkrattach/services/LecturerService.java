package ru.bk.artv.vkrattach.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.dto.LecturerDto;
import ru.bk.artv.vkrattach.dto.LecturerFullDto;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.mappers.LecturerMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LecturerService {

    LecturerMapper lecturerMapper;
    LecturerDao lecturerDao;
    OrderDao orderDao;

    public LecturerFullDto patchLecturer(LecturerFullDto lecturerFullDto) {
        Lecturer lecturer = lecturerDao.getLecturerById(lecturerFullDto.getId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("Научный руководитель с ID : " + lecturerFullDto.getId() + "не найден");
        });
        lecturerMapper.toLecturer(lecturerFullDto, lecturer);
        lecturerDao.patchLecturer(lecturer);
        return lecturerMapper.toLecturerFullDTO(lecturer);
    }

    public LecturerFullDto putLecturer(LecturerFullDto lecturerFullDto) {
        Lecturer lecturer = new Lecturer();
        lecturerMapper.toLecturer(lecturerFullDto, lecturer);
        lecturerDao.patchLecturer(lecturer);
        return lecturerMapper.toLecturerFullDTO(lecturer);
    }

    @Transactional
    public void deleteLecturer(Long id) {
        Lecturer lecturer = lecturerDao.getLecturerById(id).orElseThrow(() -> new ResourceNotFoundException("Lecturer with id : " + id + "are not found"));
        lecturerDao.deleteLecturer(lecturer);
    }

    public List<LecturerDto> getSimpleLecturers(String department) {
        List<Lecturer> lecturers = lecturerDao.getLecturers(department);
        return lecturers.stream().map(lecturer -> lecturerMapper.toLecturerDTO(lecturer)).toList();
    }


    public List<LecturerDto> getFullLecturers(String department, String faculty, String year) {
        List<Lecturer> lecturers = lecturerDao.getLecturers(department);
        if (faculty != null && department !=null && !faculty.equals("") && !department.equals("")) {
            return lecturers.stream().map(lecturer -> {
                LecturerFullDto lecturerDTO = lecturerMapper.toLecturerFullDTO(lecturer);
                lecturerDTO.setOrdersCount(orderDao.getOrders(lecturer).size());
                return lecturerDTO;
            }).collect(Collectors.toList());
        }
    return lecturers.stream().map(lecturer -> lecturerMapper.toLecturerFullDTO(lecturer)).collect(Collectors.toList());
    }
}
