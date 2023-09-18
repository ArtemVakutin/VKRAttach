package ru.bk.artv.vkrattach.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.dto.LecturerDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import ru.bk.artv.vkrattach.services.mappers.LecturerMapper;

import java.util.List;

@Service
public class LecturerService {

    LecturerMapper lecturerMapper;
    LecturerDao lecturerDao;

    public LecturerService(LecturerMapper lecturerMapper, LecturerDao lecturerDao) {
        this.lecturerMapper = lecturerMapper;
        this.lecturerDao = lecturerDao;
    }

    public List<Lecturer> getLecturers(String department) {
        return lecturerDao.getLecturersByDepartment(department);
    }

    public LecturerDTO patchLecturer(LecturerDTO lecturerDTO) {
        Lecturer lecturer = lecturerDao.getLecturerById(lecturerDTO.getId()).orElseThrow(() -> {
            throw new ResourceNotFoundException("Научный руководитель с ID : " + lecturerDTO.getId() + "не найден");
        });
        lecturerMapper.toLecturer(lecturerDTO, lecturer);
        lecturerDao.patchLecturer(lecturer);
        return lecturerMapper.toLecturerDTO(lecturer);
    }

    public LecturerDTO putLecturer(LecturerDTO lecturerDTO) {
        Lecturer lecturer = new Lecturer();
        lecturerMapper.toLecturer(lecturerDTO, lecturer);
        lecturerDao.patchLecturer(lecturer);
        return lecturerMapper.toLecturerDTO(lecturer);
    }

    @Transactional
    public void deleteLecturer(Long id) {
        Lecturer lecturer = lecturerDao.getLecturerById(id).orElseThrow(() -> new ResourceNotFoundException("Lecturer with id : " + id + "are not found"));
        lecturerDao.deleteLecturer(lecturer);
    }
}
