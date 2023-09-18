package ru.bk.artv.vkrattach.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.bk.artv.vkrattach.services.mappers.ThemesMapper;
import ru.bk.artv.vkrattach.dao.ThemesDao;
import ru.bk.artv.vkrattach.domain.Theme;
import ru.bk.artv.vkrattach.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThemesServiceTest {


    @Mock(name = "themesDao")
    ThemesDao themesDao;

    @Mock(name = "themesMapper")
    ThemesMapper themesMapper;

    @InjectMocks
    private ThemesService themesService;

    @BeforeEach
    void setUp(){
        initThemesService();
        //Можно ручками мокать
//        ThemesMapper ts = Mockito.mock(ThemesMapper.class);
        //Потом напрямую вставить рефлекшном
        //ReflectionTestUtils.setField(themesService, "themesMapper", themesMapper);
        //Можно использовать fake-объекты

    }

    @Test
    void getThemes() {
        themesService.getThemes("UPV", "NBFZOP", "2020");

        Assertions.assertThrows(RuntimeException.class,
                ()->themesService.getThemes("UPC", "NBFZOP", "2020"));

    }

    private void initThemesService() {
        when(themesDao.getThemesByDepartmentFacultyYear(any(), any(), any())).thenReturn(new ArrayList<Theme>(List.of(new Theme(), new Theme())));
        when(themesDao.getThemesByDepartmentFacultyYear(Mockito.isA(String.class), Mockito.eq("NBFZOP"), any())).thenAnswer(new Answer<List<Theme>>() {            @Override
            public List<Theme> answer(InvocationOnMock invocationOnMock) throws Throwable {
                Theme theme = new Theme();
                theme.setDepartment(invocationOnMock.getArgument(0, String.class));
                theme.setFaculty(invocationOnMock.getArgument(1, String.class));
                theme.setYearOfRecruitment(invocationOnMock.getArgument(2, String.class));
                theme.setThemeName("Прыжки в воду без парашюта");
                return new ArrayList<Theme>(List.of(theme));
            }
        });
        when(themesDao.getThemesByDepartmentFacultyYear(Mockito.eq("UPC"), any(), any()))
                .thenThrow(new ResourceNotFoundException("EXC"));
    }


}