package ru.bk.artv.vkrattach.dao;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.dao.repository.LecturerRepository;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class LecturerDao {

    LecturerRepository lecturerRepository;

    public LecturerDao(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public List<Lecturer> getLecturersByDepartment(String department) {
        try {
            List<Lecturer> lecturerByDepartment = lecturerRepository.getLecturerByDepartment(department);
            if (lecturerByDepartment.size() > 0) {
                return lecturerByDepartment;
            }
            throw new ResourceNotFoundException("Lecturers from Department : " + department + " are not found");
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("No enum constant : " +
                    department + " : in ru.bk.artv.vkrattach.domain.Department");
        }
    }

    public Optional<Lecturer> getLecturerById (Long id) {
        return lecturerRepository.findById(id);
    }

    public void patchLecturer(Lecturer lecturer){
        lecturerRepository.saveAndFlush(lecturer);
    }

    public void saveLecturer(Lecturer lecturer) {
        lecturerRepository.save(lecturer);
    }

    public void deleteLecturer(Lecturer lecturer){
        lecturerRepository.delete(lecturer);
    }

    public boolean checkLecturerIsExisted(Lecturer lecturer) {
        return lecturerRepository.existsBySurnameAndNameAndPatronymicAndDepartment(lecturer.getSurname(),
                lecturer.getName(),
                lecturer.getPatronymic(),
                lecturer.getDepartment());
    }
}
