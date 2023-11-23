package ru.bk.artv.vkrattach.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.domain.user.*;
import ru.bk.artv.vkrattach.dto.UserDTO;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public DefaultUser toDefaultUser(UserDTO request) {
        if (request.getRole() == Role.ADMIN) {
            return toAdminUser(request);
        } else if (request.getRole() == Role.MODERATOR) {
            return toModeratorUser(request);
        }
        return toSimpleUser(request);
    }

    public DefaultUser toDefaultUser(UserDTO request, DefaultUser defaultUser) {
        if (request.getRole() == Role.ADMIN) {
            return toAdminUser(request, (AdminUser) defaultUser);
        } else if (request.getRole() == Role.MODERATOR) {
            return toModeratorUser(request, (ModeratorUser) defaultUser);
        }
        return toSimpleUser(request, (SimpleUser) defaultUser);
    }

    abstract SimpleUser toSimpleUser(UserDTO request);

    @Mapping(target = "password", ignore = true)
    abstract SimpleUser toSimpleUser(UserDTO request, @MappingTarget SimpleUser user);

    abstract ModeratorUser toModeratorUser(UserDTO request);

    @Mapping(target = "password", ignore = true)
    abstract ModeratorUser toModeratorUser(UserDTO request, @MappingTarget ModeratorUser user);

    abstract AdminUser toAdminUser(UserDTO request);

    @Mapping(target = "password", ignore = true)
    abstract AdminUser toAdminUser(UserDTO request, @MappingTarget AdminUser user);

    public UserDTO toUserDTO(DefaultUser user) {
        if (user instanceof SimpleUser) {
            return UserDTOFromSimpleUser((SimpleUser) user);
        } else if (user instanceof ModeratorUser) {
            return UserDTOFromModeratorUser((ModeratorUser) user);
        }
        return UserDTOFromDefaultUser(user);

    }

    abstract UserDTO UserDTOFromDefaultUser(DefaultUser defaultUser);

    abstract UserDTO UserDTOFromModeratorUser(ModeratorUser defaultUser);

    abstract UserDTO UserDTOFromSimpleUser(SimpleUser defaultUser);


}
