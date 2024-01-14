package ru.bk.artv.vkrattach.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Для выдачи веб-приложения.
 */
@Controller
@RequestMapping(value = { "/"})
public class SinglePageController{
    @GetMapping(produces = "text/html", path = {"/", "/auth/**", "user/**", "moderator/**", "admin/**"})
    public String getPage() {
        return "index";
    }
}
