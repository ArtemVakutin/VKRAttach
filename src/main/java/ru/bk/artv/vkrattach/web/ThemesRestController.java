package ru.bk.artv.vkrattach.web;

import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.bk.artv.vkrattach.services.ThemesService;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.dto.ThemeDTO;

import java.util.List;

@RestController
@RequestMapping("/rest/theme")
public class ThemesRestController {

    ThemesService themesService;

    public ThemesRestController(ThemesService themesService) {
        this.themesService = themesService;
    }

    //Добавить столбец к темам (забито не забито)

    @GetMapping(path = "themes")
    public List<ThemeDTO> getThemes(@RequestParam("department") String department,
                                    @RequestParam("faculty") String faculty,
                                    @RequestParam("year") String year) {
        
        return themesService.getThemes(department, faculty, year);
    }

    @DeleteMapping(path = "themes")
    public List<ThemeDTO> deleteThemes(@RequestParam("department") String department,
                                       @RequestParam("faculty") String faculty,
                                       @RequestParam("year") String year) {
        return themesService.deleteThemes(department, faculty, year);
    }

    @GetMapping(path = "themes/byspecs")
    public List<ThemeDTO> getThemes(@Filter Specification<Theme> spec) {
        return themesService.getAllThemes(spec);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteTheme(@RequestParam("id") Long id) {
        themesService.deleteTheme(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThemeDTO addTheme(@RequestBody ThemeDTO themeDTO) {
        themesService.addTheme(themeDTO);
        return themesService.addTheme(themeDTO);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ThemeDTO patchTheme(@RequestBody ThemeDTO themeDTO) {
        themesService.patchTheme(themeDTO);
        return themesService.addTheme(themeDTO);
    }

}
