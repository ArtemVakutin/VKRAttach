package ru.bk.artv.vkrattach.services.mappers;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.dto.ThemeDTO;

@Slf4j
@Mapper(componentModel = "spring")
public abstract class ThemesMapperAbstract{

    abstract public Theme toTheme(@MappingTarget Theme theme, ThemeDTO themeDTO);

    abstract public Theme toTheme(ThemeDTO themeDTO);
}
