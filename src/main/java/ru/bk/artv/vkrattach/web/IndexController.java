package ru.bk.artv.vkrattach.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/login"})
public class IndexController {

    @GetMapping
    public String getIndex() {
        return "index1.html";
    }

    @GetMapping(path = "/vkrorder")
    public String getVkrOrder() {
        return "vkrorder.html";
    }
}
