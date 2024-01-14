package ru.bk.artv.vkrattach.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.services.model.Lecturer;
import ru.bk.artv.vkrattach.web.dto.LecturerDto;
import ru.bk.artv.vkrattach.web.dto.LecturerFullDto;


/**
 * Класс - мэппер для мэппинга Lecturer (руководитель ВКР) в/из Dto с помощью MapStruct.
 */
@Mapper(componentModel = "spring")
public abstract class LecturerMapper {

    public abstract void toLecturer(LecturerFullDto request, @MappingTarget Lecturer lecturer);

    public abstract Lecturer toLecturer(LecturerFullDto request);

    public abstract LecturerFullDto toLecturerFullDTO(Lecturer request);

    public abstract LecturerDto toLecturerDTO(Lecturer request);


}
