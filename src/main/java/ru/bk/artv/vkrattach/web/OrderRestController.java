package ru.bk.artv.vkrattach.web;


import com.turkraft.springfilter.boot.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.services.model.Order;
import ru.bk.artv.vkrattach.services.model.user.AdminUser;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.ModeratorUser;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.web.dto.OrderDTO;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.services.OrderService;
import ru.bk.artv.vkrattach.services.TokenUserToDefaultUserConverter;

import java.util.List;


/**
 * CRUD для заявок, а также получение списков заявок по разным параметрам.
 */
@Slf4j
@RestController
@RequestMapping("/rest/order")
public class OrderRestController {

    //Сервис для работы с Lecturer
    private final OrderService orderService;

    //Конвертер JWT аутентификации в расширенные данные пользователя
    private final TokenUserToDefaultUserConverter converter;

    public OrderRestController(OrderService orderService, TokenUserToDefaultUserConverter converter) {
        this.orderService = orderService;
        this.converter = converter;
    }

    /**
     * Запрос заявок по айдишнику пользователя
     *
     * @param userId    айди пользователя, заявки которого запрашиваются
     * @param tokenUser аутентифицированный пользователь
     * @return Список заявок пользователя
     */
    // TODO: 10.12.2023 Переработать под ветвление в зависимости от роли
    @GetMapping(path = "/getorders", params = "userId")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public List<OrderDTO> getOrdersById(@RequestParam Long userId, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);

        return switch (user.getRole()) {
            case USER -> orderService.getOrders((SimpleUser) user);
            case MODERATOR, ADMIN -> orderService.getOrders(userId);
        };
    }

    /**
     * Запрос заявок по фильтрам
     *
     * @param spec фильтры
     * @return список заявок
     */
    @GetMapping(path = "/getorders")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<OrderDTO> getOrders(@Filter Specification<Order> spec) {
        return orderService.getOrders(spec);
    }

    /**
     * Модификация заявки
     *
     * @param orderDTO изменяемая заявка
     * @return измененная заявка
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public OrderDTO patchOrder(@RequestBody OrderDTO orderDTO) {
        orderService.patchOrder(orderDTO);
        return orderDTO;
    }

    /**
     * Добавление заявки
     *
     * @param orderDTO  Добавляемая заявка
     * @param tokenUser аутентифицированный пользователь
     * @return добавленную заявку
     */
    // TODO: 10.12.2023 Позволяет несколько раз делать заявки, если таковые имеются
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public OrderDTO addOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);

        return switch (user.getRole()) {
            case USER -> orderService.addOrderByUser(orderDTO, (SimpleUser) user);
            case ADMIN -> orderService.addOrderByAdmin(orderDTO);
            case MODERATOR -> {
                if (((ModeratorUser) user).getDepartment().equals(orderDTO.getDepartment())) {
                    yield orderService.addOrderByAdmin(orderDTO);
                }
                throw new ResourceNotSavedException("Moderator department isn't equals with order department");
            }
        };
    }

    /**
     * @param tokenUser аутентифицируемый юзер
     * @param id        удаляемая заявка
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public void deleteOrder(@AuthenticationPrincipal TokenUser tokenUser, @RequestParam("id") Long id) {
        DefaultUser user = converter.convertToDefaultUser(tokenUser);

        switch (user.getRole()) {
            case MODERATOR,ADMIN -> orderService.deleteOrders(id);
            case USER -> orderService.deleteOrders(id, user);
        }
    }

}

