package ru.bk.artv.vkrattach.business;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bk.artv.vkrattach.dao.LecturerDao;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LecturerService {

    LecturerDao lecturerDao;

    public LecturerService(LecturerDao lecturerDao) {
        this.lecturerDao = lecturerDao;
    }

    public Map<Long, Lecturer> getLecturers(String department) {

        List<Lecturer> lecturersByDepartment = lecturerDao.getLecturersByDepartment(department);
        return lecturerDao.getLecturersByDepartment(department).stream().collect(Collectors.toMap(Lecturer::getId, lecturer -> lecturer));
    }

    public void patchLecturer(Lecturer lecturer){
        lecturerDao.patchLecturer(lecturer);
    }

    @Transactional
    public void deleteLecturer(Long id) {
        Lecturer lecturer = lecturerDao.getLecturerById(id).orElseThrow(() -> new ResourceNotFoundException("Lecturer with id : " + id + "are not found"));
        lecturer.setDeleted(true);
        lecturerDao.saveLecturer(lecturer);
    }

    @Transactional
    public void recoverLecturer(Long id) {
        Lecturer lecturer = lecturerDao.getLecturerById(id).orElseThrow(() -> new ResourceNotFoundException("Lecturer with id : " + id + "are not found"));
        lecturer.setDeleted(false);
        lecturerDao.saveLecturer(lecturer);
    }

}
