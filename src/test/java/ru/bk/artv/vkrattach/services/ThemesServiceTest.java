package ru.bk.artv.vkrattach.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import ru.bk.artv.vkrattach.dao.OrderDao;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.services.mappers.ThemesMapper;
import ru.bk.artv.vkrattach.services.model.Theme;
import ru.bk.artv.vkrattach.testutils.OrderBuilder;
import ru.bk.artv.vkrattach.testutils.ThemeBuilder;
import ru.bk.artv.vkrattach.testutils.ThemeDtoBuilder;
import ru.bk.artv.vkrattach.web.dto.ThemeDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemesServiceTest {

    @Mock
    ThemesDao themesDao;

    @Mock
    ThemesMapper themesMapper;

    @Mock
    OrderDao orderDao;

    @InjectMocks
    ThemesService themesService;

    @Test
    void getThemes_GivenThreeArgs_ReturnsThemes() {
        // given
        String department = "УПв";
        String faculty = "НБФЗОП";
        String year = "2019";

        Theme theme1 = ThemeBuilder.create().withThemeId(1L).build();
        Theme theme2 = ThemeBuilder.create().withThemeId(2L).build();
        List<Theme> themes = Arrays.asList(theme1, theme2);

        ThemeDto themeDto1 = ThemeDtoBuilder.create().build();
        ThemeDto themeDto2 = ThemeDtoBuilder.create().build();


        when(themesDao.getThemesByDepartmentFacultyYear(department, faculty, year)).thenReturn(themes);
        when(themesMapper.themeToThemeDTO(eq(theme1))).thenReturn(themeDto1);
        when(themesMapper.themeToThemeDTO(eq(theme2))).thenReturn(themeDto2);

        // when
        List<ThemeDto> result = themesService.getThemes(department, faculty, year);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void getAllThemes_GivenSpecification_ReturnsAllThemes() {
        // given

        Specification<Theme> spec = (root, query, criteriaBuilder) -> null;

        Theme theme1 = ThemeBuilder.create().withThemeId(1L).build();
        Theme theme2 = ThemeBuilder.create().withThemeId(2L).build();
        List<Theme> themes = Arrays.asList(theme1, theme2);

        ThemeDto themeDto1 = ThemeDtoBuilder.create().build();
        ThemeDto themeDto2 = ThemeDtoBuilder.create().withIsBusy(true).build();

        when(themesDao.getAllThemes(spec)).thenReturn(themes);
        when(themesMapper.themeToThemeDTO(eq(theme1))).thenReturn(themeDto1);
        when(themesMapper.themeToThemeDTO(eq(theme2))).thenReturn(themeDto2);

        // when
        List<ThemeDto> result = themesService.getAllThemes(spec);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void deleteTheme_GivenThemeId_SuccessfulDeletion() {
        // given
        Long themeId = 1L;
        Optional<Theme> build = Optional.ofNullable(ThemeBuilder.create().build());


        when(themesDao.getThemeById(any())).thenReturn(build);
        when(orderDao.isOrderExists(any())).thenReturn(false);

        // when
        themesService.deleteTheme(themeId);

        // then
        verify(themesDao, times(1)).getThemeById(any());
        verify(themesDao, times(1)).deleteTheme(themeId);
        verify(orderDao, times(1)).isOrderExists(any());
    }

    @Test
    void deleteTheme_GivenAttachedThemeId_ThrowsException() {
        // given
        Long themeId = 1L;
        Optional<Theme> build = Optional.ofNullable(ThemeBuilder.create().build());


        when(themesDao.getThemeById(any())).thenReturn(build);
        when(orderDao.isOrderExists(any())).thenReturn(true);

        // then
        assertThrows(ResourceNotSavedException.class, ()->themesService.deleteTheme(themeId));
        verify(themesDao, times(0)).deleteTheme(themeId);
    }


    @Test
    void addTheme_ReturnsAddedTheme() {
        // given
        ThemeDto themeDto = ThemeDtoBuilder.create().build();
        Theme theme = ThemeBuilder.create().build();
        when(themesMapper.toTheme(any())).thenReturn(theme);
        when(themesMapper.themeToThemeDTO(any())).thenReturn(themeDto);

        // when
        ThemeDto result = themesService.addTheme(themeDto);

        // then
        assertNotNull(result);
        verify(themesMapper, times(1)).toTheme(any());
        verify(themesDao, times(1)).addTheme(any());
        verify(themesDao, times(1)).isThemeExist(any());
    }

    @Test
    void addExistedTheme_ThrowsException() {
        // given
        ThemeDto themeDto = ThemeDtoBuilder.create().build();
        Theme theme = ThemeBuilder.create().build();
        when(themesMapper.toTheme(any())).thenReturn(theme);
        when(themesDao.isThemeExist(any())).thenReturn(true);

        // then
        assertThrows(ResourceNotSavedException.class, ()->themesService.addTheme(themeDto));
        verify(themesMapper, times(1)).toTheme(any());
        verify(themesDao, times(0)).addTheme(any());
        verify(themesDao, times(1)).isThemeExist(any());
    }

    @Test
    void patchTheme_ReturnsPatchedTheme() {
        // given
        ThemeDto themeDto = ThemeDtoBuilder.create().withId(1L).build();
        Theme theme = ThemeBuilder.create().withThemeId(1L).build();

        when(themesDao.getThemeById(eq(1L))).thenReturn(Optional.of(theme));
        when(themesMapper.toTheme(any(), any())).thenReturn(theme);
        when(themesMapper.themeToThemeDTO(any())).thenReturn(themeDto);

        // when
        ThemeDto result = themesService.patchTheme(themeDto);

        // then
        assertNotNull(result);
        verify(themesDao, times(1)).getThemeById(eq(1L));
        verify(themesMapper, times(1)).toTheme(any(), any());
        verify(themesDao, times(1)).addTheme(any());
        verify(themesDao, times(1)).isThemeExist(any());
    }

    @Test
    void deleteThemes_ReturnsThemesToDelete() {
        // given
        String department = "НБФЗОП";
        String faculty = "УПв";
        String year = "2019";

        Theme theme1 = ThemeBuilder.create().withThemeId(1L).build();
        Theme theme2 = ThemeBuilder.create()
                .withThemeId(1L)
                .withOrders(Collections.singletonList(OrderBuilder.create().build()))
                .build();
        ThemeDto themeDto = ThemeDtoBuilder.create().build();

        List<Theme> themes = Arrays.asList(theme1, theme2);
        when(themesDao.getThemesByDepartmentFacultyYear(anyString(), anyString(), anyString())).thenReturn(themes);
        when(orderDao.isOrderExists(eq(theme1))).thenReturn(false);
        when(orderDao.isOrderExists(eq(theme2))).thenReturn(true);
        when(themesMapper.themeToThemeDTO(any())).thenReturn(themeDto);

        // when
        List<ThemeDto> result = themesService.deleteThemes(department, faculty, year);

        // then
        assertEquals(1, result.size());
    }
}
