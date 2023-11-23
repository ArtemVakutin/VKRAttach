package ru.bk.artv.vkrattach.web;


import com.turkraft.springfilter.boot.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.services.OrderService;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.user.AdminUser;
import ru.bk.artv.vkrattach.dto.OrderDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/rest/order")
public class OrderRestController {

    OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/getorders", params = "userId")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDTO> getOrdersById (@RequestParam Long userId, @AuthenticationPrincipal DefaultUser user) {
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
    public OrderDTO addOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal DefaultUser user) {
        log.info(user.toString());
        log.info(orderDTO.toString());
        if (user instanceof SimpleUser) {
            log.info(user.getClass().toString());
            log.info(user + "    " + "SIMPLE USER");
            orderService.addOrderByUser(orderDTO, (SimpleUser) user);
        } else if (user instanceof AdminUser) {
            orderService.addOrderByAdmin(orderDTO);
            log.info(user.getClass().toString());
            log.info(user + "    " + "ADMIN USER");
        } else {
            log.info(user.getClass().toString());
            log.info(user + "    " + "DEFAULT USER");
        }
        return orderDTO;
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrder(@AuthenticationPrincipal DefaultUser user, @RequestParam("id") Long id){
        orderService.deleteOrders(id, user);
    }

}

