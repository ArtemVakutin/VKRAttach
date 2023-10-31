package ru.bk.artv.vkrattach.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
//@RequestMapping("/page")
public class SinglePageController {

//    @GetMapping
//    @RequestMapping(produces = "text/html")
    public String getPage() {
        return "index";
    }

}
