package ru.bk.artv.vkrattach.services.mappers;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.web.dto.ThemeDto;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;

/**
 *
 */
@Slf4j
//@AllArgsConstructor
@Mapper(componentModel = "spring")
public abstract class ThemesMapper {

    //Метод проверяет, чтобы количество неотклоненных заявок было не больше 1.
    public ThemeDto themeToThemeDTO(Theme theme) {
        log.info(theme.toString());
        ThemeDto themeDTO = new ThemeDto();
        themeDTO.setId(theme.getThemeId());
        themeDTO.setThemeName(theme.getThemeName());
        themeDTO.setDepartment(theme.getDepartment());
        themeDTO.setFaculty(theme.getFaculty());
        themeDTO.setYear(theme.getYear());
        if (theme.getOrders() != null && !theme.getOrders().isEmpty()) {
            List<Order> orders = theme.getOrders().stream().filter((order -> order.getRequestStatus() != Order.RequestStatus.REFUSED)).toList();
            if (orders.size() > 1) {
                throw new ResourceNotFoundException("in theme : " + theme.getThemeId() + " non refused orders > 1");
            } else if(!orders.isEmpty()){
                themeDTO.setBusy(true);
            }
        } else {
            themeDTO.setBusy(false);
        }
        return themeDTO;
    }

    abstract public Theme toTheme(@MappingTarget Theme theme, ThemeDto themeDTO);

    abstract public Theme toTheme(ThemeDto themeDTO);
}
