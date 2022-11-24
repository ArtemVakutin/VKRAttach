package ru.bk.artv.vkrattach.web;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.business.OrderService;
import ru.bk.artv.vkrattach.domain.User;
import ru.bk.artv.vkrattach.domain.dto.OrderDTO;

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
    public Map<Long, String> getLecturers(@RequestParam("department") String department) {
        return orderService.getLecturers(department);
    }

    @GetMapping(path = "/themes", produces = "application/json")
    public Map<Long, String> getThemes(@RequestParam("department") String department,
                                       @RequestParam("faculty") String faculty) {
        return orderService.getThemes(department, faculty);
    }

    @PostMapping(path = "/addorder", consumes = "application/json", produces = "application/json")
    public OrderDTO addOrder(@RequestBody OrderDTO orderDTO,  @AuthenticationPrincipal User user){
        orderService.addOrder(orderDTO, user);
        return orderDTO;
    }

}

