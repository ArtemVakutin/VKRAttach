package ru.bk.artv.vkrattach.web;


import com.turkraft.springfilter.boot.Filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.business.OrderService;
import ru.bk.artv.vkrattach.domain.Order;
import ru.bk.artv.vkrattach.domain.dto.OrderDTO;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/rest/order")
public class OrderRestController {

    OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/getorders")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<Long, OrderDTO> getOrders(@Filter Specification<Order> spec){
        return orderService.getOrders(spec);
    }

    @GetMapping(path = "/user/getorders")
    @ResponseStatus(HttpStatus.OK)
    public Map<Long, OrderDTO> getOrdersForUser(@AuthenticationPrincipal SimpleUser user) {
        return orderService.getOrders(user);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO patchOrder(@RequestBody OrderDTO orderDTO) {
        orderService.patchOrder(orderDTO);
        return orderDTO;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO addOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal SimpleUser user) {
        log.info(user.toString());
        log.info(orderDTO.toString());
        orderService.addOrder(orderDTO, user);
        return orderDTO;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrders(@AuthenticationPrincipal DefaultUser user, @RequestParam("id") Long id){
        orderService.deleteOrders(id, user);
    }

}

