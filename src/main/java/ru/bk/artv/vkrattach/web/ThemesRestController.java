package ru.bk.artv.vkrattach.web;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bk.artv.vkrattach.business.ThemesService;
import java.util.Map;

@RestController
@RequestMapping("/rest/theme")
public class ThemesRestController {

    ThemesService themesService;

    public ThemesRestController(ThemesService themesService) {
        this.themesService = themesService;
    }

    @GetMapping(path = "getthemes")
    public Map<String, String> getThemes(@RequestParam("department") String department,
                                         @RequestParam("faculty") String faculty,
                                         @RequestParam("year") String year) {
        return themesService.getThemes(department, faculty, year);
    }
}
