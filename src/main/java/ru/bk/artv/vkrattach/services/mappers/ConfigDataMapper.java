package ru.bk.artv.vkrattach.services.mappers;

import org.mapstruct.Mapper;
import ru.bk.artv.vkrattach.services.model.ConfigData;
import ru.bk.artv.vkrattach.web.dto.ConfigDataDto;

@Mapper(componentModel = "spring")
public abstract class ConfigDataMapper {

    public abstract ConfigDataDto toConfigDataDto(ConfigData configData);

    public abstract ConfigData toConfigData(ConfigDataDto configDataDto);

}
