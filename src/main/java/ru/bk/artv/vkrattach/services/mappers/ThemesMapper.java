package ru.bk.artv.vkrattach.services.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Service;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.dto.ThemeDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ThemesMapper {

    OrderMapper orderMapper;
    ThemesMapperAbstract themesMapper;

    //Метод проверяет, чтобы количество неотклоненных заявок было не больше 1.
    public ThemeDTO themeToThemeDTO(Theme theme) {
        log.info(theme.toString());
        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setId(theme.getThemeId());
        themeDTO.setThemeName(theme.getThemeName());
        themeDTO.setDepartment(theme.getDepartment());
        themeDTO.setFaculty(theme.getFaculty());
        themeDTO.setYear(theme.getYear());
        if (theme.getOrders() != null && !theme.getOrders().isEmpty()) {
            List<Order> orders = theme.getOrders().stream().filter((order -> order.getOrderStatus() != Order.OrderStatus.REFUSED)).collect(Collectors.toList());
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

    public Theme toTheme(Theme theme, ThemeDTO themeDTO) {
        return themesMapper.toTheme(theme, themeDTO);
    }

    public Theme toTheme(ThemeDTO themeDTO) {
        return themesMapper.toTheme(themeDTO);
    }
}
