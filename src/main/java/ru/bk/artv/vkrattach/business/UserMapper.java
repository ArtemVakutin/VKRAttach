package ru.bk.artv.vkrattach.business;

import org.mapstruct.Mapper;
import ru.bk.artv.vkrattach.domain.User;
import ru.bk.artv.vkrattach.domain.dto.UserDTO;
import ru.bk.artv.vkrattach.domain.dto.UserRegistrationDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegistrationDTO request);
    UserDTO toUserDTO(User user);
}
