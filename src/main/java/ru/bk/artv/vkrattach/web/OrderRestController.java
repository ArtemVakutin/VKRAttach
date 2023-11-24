package ru.bk.artv.vkrattach.web;


import com.turkraft.springfilter.boot.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.config.security.auth.TokenUser;
import ru.bk.artv.vkrattach.domain.user.ModeratorUser;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.services.OrderService;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.user.AdminUser;
import ru.bk.artv.vkrattach.dto.OrderDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

import java.util.List;
import java.util.function.Function;


@Slf4j
@RestController
@RequestMapping("/rest/order")
public class OrderRestController {

    OrderService orderService;
    Function<TokenUser, DefaultUser> converter;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/getorders", params = "userId")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getOrdersById (@RequestParam Long userId, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.apply(tokenUser);
        return orderService.getOrders(userId, user);
    }

    @GetMapping(path = "/getorders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getOrders(@Filter Specification<Order> spec){
                return orderService.getOrders(spec);
    }


    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO patchOrder(@RequestBody OrderDTO orderDTO) {
        orderService.patchOrder(orderDTO);
        return orderDTO;
    }

    //to do
    //Позволяет несколько раз делать заявки на неотклоненные ордеры
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO addOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal TokenUser tokenUser) {
        DefaultUser user = converter.apply(tokenUser);

        if (user instanceof SimpleUser) {
            return orderService.addOrderByUser(orderDTO, (SimpleUser) user);
        } else if (user instanceof AdminUser ||
                (user instanceof ModeratorUser &&
                        ((ModeratorUser) user).getDepartment().equals(orderDTO.getDepartment()))) {
            return orderService.addOrderByAdmin(orderDTO);
        } else {
            throw new ResourceNotSavedException("Moderator department isn't equals with order department");
        }
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(@AuthenticationPrincipal TokenUser tokenUser, @RequestParam("id") Long id){
        DefaultUser user = converter.apply(tokenUser);
        orderService.deleteOrders(id, user);
    }

}

