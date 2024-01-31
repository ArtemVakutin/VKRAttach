package ru.bk.artv.vkrattach.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.bk.artv.vkrattach.dao.repository.DomainDataRepository;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.services.mappers.ConfigDataMapper;
import ru.bk.artv.vkrattach.services.model.ConfigData;
import ru.bk.artv.vkrattach.web.dto.ConfigDataDto;

import java.util.List;

/**
 * Необходим для управления конфигурацией приложения из окна клиента (администратора)
 */
@Getter
@Service
public class ConfigDataCrudService {

    private final DomainDataRepository domainDataRepository;
    private final ConfigDataMapper configDataMapper;

    public ConfigDataCrudService(DomainDataRepository domainDataRepository, ConfigDataMapper configDataMapper) {
        this.domainDataRepository = domainDataRepository;
        this.configDataMapper = configDataMapper;
    }

    /**
     * Возвращает полную конфигурацию для редактирования админом. Конфигурация для работы приложения клиента возвращается
     * из @ConfigDataService.class
     *
     * @param type тип данных
     * @return полную конфигурацию для редактирования админом
     */
    public List<ConfigData> getDataByDataType(ConfigData.ConfigType type) {
        return domainDataRepository.getByType(type);
    }

    /**
     * Сохраняет изменения в единицу конфигурации приложения. Предполагается, что value не редактируется, редактируется
     * только label
     *
     * @param configDataDto единица конфигурации приложения
     */
    public ConfigDataDto patchData(ConfigDataDto configDataDto) {
        ConfigData configData = domainDataRepository.findById(configDataDto.getId())
                .orElseThrow(() -> new ResourceNotSavedException("Конфигурация с таким id отсутствует, невозможно изменить"));
        configData.setLabel(configDataDto.getLabel());
        configData.setValue(configDataDto.getValue());

        domainDataRepository.saveAndFlush(configData);
        return configDataMapper.toConfigDataDto(configData);
    }

    /**
     * Добавляет новую единицу конфигурации приложения. Проверяется, имеется ли уже такое же сочетание type-value
     *
     * @param configDataDto единица конфигурации приложения
     */
    public ConfigDataDto putData(ConfigDataDto configDataDto) {
        if (domainDataRepository.existsByTypeAndValue(configDataDto.getType(), configDataDto.getValue())) {
            throw new ResourceNotSavedException("Конфигурация с такими данными уже существует");
        }
        ConfigData configData = configDataMapper.toConfigData(configDataDto);
        domainDataRepository.saveAndFlush(configData);
        return configDataMapper.toConfigDataDto(configData);
    }

    /**
     * Удаляет единицу конфигурации приложения.
     *
     * @param id айдишник единицы конфигурации
     */
    public void deleteData(Long id) {
        domainDataRepository.deleteById(id);
    }
}
