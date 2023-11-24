package ru.bk.artv.vkrattach.config.security.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {

    @GetMapping("manager")
    public String getManagerPage() {
        return "manager";
    }
}
