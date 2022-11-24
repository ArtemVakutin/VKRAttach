package ru.bk.artv.vkrattach.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;
import ru.bk.artv.vkrattach.domain.User;

import java.security.Principal;

@Controller
//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
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
