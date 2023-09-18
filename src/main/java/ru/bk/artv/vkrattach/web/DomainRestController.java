package ru.bk.artv.vkrattach.web;

import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.domain.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "rest/domain")
public class DomainRestController {

AllDomainData allDomainData;


    public DomainRestController(AllDomainData allDomainData) {
        this.allDomainData = allDomainData;
    }

    @GetMapping
    public AllDomainData getDepartments() {
        return allDomainData;
    }
}
