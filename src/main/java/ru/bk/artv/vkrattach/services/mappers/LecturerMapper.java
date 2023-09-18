package ru.bk.artv.vkrattach.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.dto.LecturerDTO;

@Mapper(componentModel = "spring")
public abstract class LecturerMapper {

    public abstract Lecturer toLecturer(LecturerDTO request, @MappingTarget Lecturer lecturer);

    public abstract Lecturer toLecturer(LecturerDTO request);

    public abstract LecturerDTO toLecturerDTO(Lecturer request);
}
