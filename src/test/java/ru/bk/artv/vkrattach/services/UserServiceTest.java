package ru.bk.artv.vkrattach.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.bk.artv.vkrattach.dao.UserDao;
import ru.bk.artv.vkrattach.exceptions.ResourceNotPatchedException;
import ru.bk.artv.vkrattach.exceptions.ResourceNotSavedException;
import ru.bk.artv.vkrattach.exceptions.UserNotAuthorizedException;
import ru.bk.artv.vkrattach.services.mappers.UserMapper;
import ru.bk.artv.vkrattach.services.model.user.AdminUser;
import ru.bk.artv.vkrattach.services.model.user.DefaultUser;
import ru.bk.artv.vkrattach.services.model.user.Role;
import ru.bk.artv.vkrattach.services.model.user.SimpleUser;
import ru.bk.artv.vkrattach.testutils.UserBuilder;
import ru.bk.artv.vkrattach.testutils.UserDtoBuilder;
import ru.bk.artv.vkrattach.web.dto.UserDto;

import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserMapper userMapper;
    @Mock
    UserDao userDao;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void registerNewUser_GivenUserForRegistration_ReturnsUserDto() {
        //given
        UserDto userDto = UserDtoBuilder.create().build();
        UserDto userDtoRegistered = UserDtoBuilder.create().withId(1L).build();
        SimpleUser simpleUser = UserBuilder.create().buildSimpleUser();
        when(this.userMapper.toUserDTO(any(SimpleUser.class))).thenReturn(userDtoRegistered);
        when(this.userMapper.toDefaultUser(any())).thenReturn(simpleUser);
        when(this.userDao.checkLoginExisted(any())).thenReturn(false);
        when(this.userDao.saveUser(any())).thenReturn(1L);

        //then
        UserDto result = userService.registerNewUser(userDto);

        //when
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void registerNewUser_GivenUserForRegistrationWithId_ThrowsException() {
        //given
        UserDto userDto = UserDtoBuilder.create().withId(1L).build();
        //when
        assertThrows(ResourceNotSavedException.class, () -> userService.registerNewUser(userDto));
    }

    @Test
    void registerNewUser_GivenUserForRegistrationWithExistedLogin_ThrowsException() {
        //given
        UserDto userDto = UserDtoBuilder.create().withLogin("userExisted").build();
        when(this.userDao.checkLoginExisted(eq("userExisted"))).thenReturn(true);
        //when
        assertThrows(ResourceNotSavedException.class, () -> userService.registerNewUser(userDto));
    }

    @Test
    void getUser_GivenValidUser_ReturnsUserDto() {
        //given
        UserDto userDtoRegistered = UserDtoBuilder.create().withId(1L).build();
        SimpleUser simpleUser = UserBuilder.create().buildSimpleUser();
        when(this.userMapper.toUserDTO(any(SimpleUser.class))).thenReturn(userDtoRegistered);

        //then
        UserDto result = userService.getUser(simpleUser);

        //when
        assertNotNull(result);
    }

    @Test
    void getUser_GivenNullUser_ThrowsException() {
        //given
        SimpleUser simpleUser = null;

        //when
        assertThrows(UserNotAuthorizedException.class, ()->userService.getUser(simpleUser));
    }

    @Test
    void getUser_GivenSimpleUserId_ReturnsUserDto() {
        //given
        Long id = 1L;
        UserDto userDtoRegistered = UserDtoBuilder.create().withId(1L).build();
        SimpleUser simpleUser = UserBuilder.create().buildSimpleUser();
        when(this.userDao.findUserById(any())).thenReturn(simpleUser);
        when(this.userMapper.toUserDTO(any(SimpleUser.class))).thenReturn(userDtoRegistered);

        //then
        UserDto result = userService.getUser(id);

        //when
        assertNotNull(result);
    }

    @Test
    void getUser_GivenAdminUserId_ThrowsAccessDeniedException() {
        //given
        Long id = 1L;
        AdminUser adminUser = UserBuilder.create().buildAdminUser();
        when(this.userDao.findUserById(any())).thenReturn(adminUser);

        //when
        assertThrows(AccessDeniedException.class, ()->userService.getUser(id));
    }

    @Test
    void patchUser_GivenUserDto_ReturnsPatchedUserDto() {
        //given
        UserDto userDto = UserDtoBuilder.create().withPassword("11111").withId(1L).build();
        DefaultUser defaultUser = UserBuilder.create().buildSimpleUser();
        when(this.userDao.findUserById(any())).thenReturn(defaultUser);
        when(this.userMapper.toDefaultUser(any(), any())).thenReturn(defaultUser);
        when(this.passwordEncoder.encode(any())).thenReturn("11111");
        when(this.userDao.saveUser(any())).thenReturn(1L);
        when(this.userMapper.toUserDTO(any(SimpleUser.class))).thenReturn(userDto);

        //then
        UserDto result = userService.patchUser(userDto);

        //when
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void patchUser_GivenDefaultUserAndUserDto_ReturnsPatchedUserDto() {
        //given
        UserDto userDto = UserDtoBuilder.create().withPassword("11111").withOldPassword("11111").withId(1L).build();
        DefaultUser defaultUser = UserBuilder.create().withId(1L).buildSimpleUser();
        when(this.userMapper.toDefaultUser(any(), any())).thenReturn(defaultUser);
        when(this.passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0).equals(invocation.getArgument(1)));
        when(this.passwordEncoder.encode(any())).thenReturn("11111");
        when(this.userDao.saveUser(any())).thenReturn(1L);
        when(this.userMapper.toUserDTO(any(SimpleUser.class))).thenReturn(userDto);

        //then
        UserDto result = userService.patchUser(userDto, defaultUser);

        //when
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    void patchUser_GivenDefaultUserAndUserDtoWithDifferentOldAndNewPassword_ThrowsException() {
        //given
        UserDto userDto = UserDtoBuilder.create().withPassword("11111").withOldPassword("1111").withId(1L).build();
        DefaultUser defaultUser = UserBuilder.create().withId(1L).buildSimpleUser();
        when(this.userMapper.toDefaultUser(any(), any())).thenReturn(defaultUser);
        when(this.passwordEncoder.matches(anyString(), anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0).equals(invocation.getArgument(1)));

        //when
        assertThrows(ResourceNotPatchedException.class, () -> userService.patchUser(userDto, defaultUser));
    }

    @Test
    void patchUser_GivenDefaultUserAndUserDtoWithDifferentId_ThrowsException() {
        //given
        UserDto userDto = UserDtoBuilder.create().withId(1L).build();
        DefaultUser defaultUser = UserBuilder.create().withId(2L).buildSimpleUser();

        //when
        assertThrows(AccessDeniedException.class, () -> userService.patchUser(userDto, defaultUser));
    }

    @Test
    void findUsers_GivenSpecification_ReturnListUsersDto() {
        //given
        Specification<SimpleUser> specification = (root, query, criteriaBuilder) -> null;
        when(this.userDao.findSimpleUsers(any()))
                .thenReturn(new ArrayList<>(Arrays.asList(UserBuilder.create().buildSimpleUser())));
        when(userMapper.toUserDTO(any())).thenReturn(UserDtoBuilder.create().build());

        //then
        List<UserDto> users = userService.findUsers(specification);

        //when
        assertFalse(users.isEmpty());
    }

    @Test
    void findUsers_GivenRole_ReturnListUsersDto() {
        //given
        when(this.userDao.findDefaultUsers(any()))
                .thenReturn(new ArrayList<>(Collections.singletonList(UserBuilder.create().buildSimpleUser())));
        when(userMapper.toUserDTO(any())).thenReturn(UserDtoBuilder.create().build());

        //then
        List<UserDto> users = userService.findUsers(Role.USER);

        //when
        assertFalse(users.isEmpty());
    }

    @Test
    void deleteUser_GivenId_SuccesfullDeleting() {
        //then
        userService.deleteUser(1L);
        //when
        verify(userDao, times(1)).deleteUser(any());
    }
}