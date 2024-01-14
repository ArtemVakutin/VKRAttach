package ru.bk.artv.vkrattach.web;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.bk.artv.vkrattach.config.security.TokenUser;
import ru.bk.artv.vkrattach.services.TokenUserToDefaultUserConverterImpl;
import ru.bk.artv.vkrattach.services.UserService;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.testutils.TokenUserBuilder;
import ru.bk.artv.vkrattach.testutils.UserBuilder;
import ru.bk.artv.vkrattach.testutils.UserDtoBuilder;
import ru.bk.artv.vkrattach.web.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UserRestController.class)
class UserRestControllerTest {


    @MockBean
    private UserService userService;
    @MockBean
    private Validator validator;
    @MockBean
    private TokenUserToDefaultUserConverterImpl converter;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetUser_GivenRoleUserPrincipal_ReturnStatus200() throws Exception {
        // Mock authenticated user
        TokenUser principal = TokenUserBuilder.create().buildTokenUserRoleUser();
        UserDto build = UserDtoBuilder.create().build();
        SimpleUser simpleUser = UserBuilder.create().buildSimpleUser();
        when(converter.convertToDefaultUser(any())).thenReturn(simpleUser);
        when(userService.getUser((DefaultUser) any())).thenReturn(build);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/user")
                        .with(SecurityMockMvcRequestPostProcessors.user(principal)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetUser_Given_ReturnStatus200() throws Exception {
        // Mock authenticated user
        TokenUser principal = TokenUserBuilder.create().buildTokenUserRoleUser();
        UserDto build = UserDtoBuilder.create().build();
        SimpleUser simpleUser = UserBuilder.create().buildSimpleUser();
        when(converter.convertToDefaultUser(any())).thenReturn(simpleUser);
        when(userService.getUser((DefaultUser) any())).thenReturn(build);

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/user")
                        .with(SecurityMockMvcRequestPostProcessors.user(principal)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void testGetUser_GivenAnonymous_ReturnStatus403() throws Exception {
        // Mock authenticated user
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/user"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void getUsers() {
    }

    @Test
    void addUser() {
    }

    @Test
    void patchUser() {
    }

    @Test
    void deleteUser() {
    }
}