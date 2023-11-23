package ru.bk.artv.vkrattach.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.domain.Lecturer;
import ru.bk.artv.vkrattach.dto.LecturerDto;
import ru.bk.artv.vkrattach.dto.LecturerFullDto;

@Mapper(componentModel = "spring")
public abstract class LecturerMapper {

    public abstract Lecturer toLecturer(LecturerFullDto request, @MappingTarget Lecturer lecturer);

    public abstract Lecturer toLecturer(LecturerFullDto request);

    public abstract LecturerFullDto toLecturerFullDTO(Lecturer request);

    public abstract LecturerDto toLecturerDTO(Lecturer request);


}
