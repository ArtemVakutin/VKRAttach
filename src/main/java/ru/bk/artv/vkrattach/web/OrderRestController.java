package ru.bk.artv.vkrattach.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.business.OrderService;
import ru.bk.artv.vkrattach.domain.User;
import ru.bk.artv.vkrattach.domain.dto.OrderDTO;

import java.time.LocalDate;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/rest/order")
public class OrderRestController {

    OrderService orderService;

    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/departments", produces = "application/json")
    public Map<String, String> getDepartments() {
        return orderService.getDepartments();
    }

    @GetMapping(path = "/lecturers", produces = "application/json")
    public Map<String, String> getLecturers(@RequestParam("department") String department) {
        return orderService.getLecturers(department);
    }

    @GetMapping(path = "/themes", produces = "application/json")
    public Map<String, String> getThemes(@RequestParam("department") String department,
                                         @RequestParam("faculty") String faculty,
                                         @RequestParam("year") String year) {
        return orderService.getThemes(department, faculty, year);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO addOrder(@RequestBody OrderDTO orderDTO, @AuthenticationPrincipal User user) {
        log.info(user.toString());
        log.info(orderDTO.toString());
        orderService.addOrder(orderDTO, user);
        return orderDTO;
    }

    @GetMapping(path = "/getorders", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, OrderDTO> getOrders(@AuthenticationPrincipal User user) {
        return orderService.getOrders(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrders(@AuthenticationPrincipal User user, @RequestParam("id") Long id){
        orderService.deleteOrders(id, user);
    }

}

