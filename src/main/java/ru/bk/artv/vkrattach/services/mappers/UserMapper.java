package ru.bk.artv.vkrattach.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.web.dto.UserDto;
import ru.bk.artv.vkrattach.services.model.user.*;

/**
 * Класс - мэппер для мэппинга User-ов (темы ВКР) в/из Dto с помощью MapStruct. Ибо необходима проверка на занятость темы и т.д.
 */
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public DefaultUser toDefaultUser(UserDto request) {
        if (request.getRole() == Role.ADMIN) {
            return toAdminUser(request);
        } else if (request.getRole() == Role.MODERATOR) {
            return toModeratorUser(request);
        }
        return toSimpleUser(request);
    }

    public DefaultUser toDefaultUser(UserDto request, DefaultUser defaultUser) {
        if (request.getRole() == Role.ADMIN) {
            return toAdminUser(request, (AdminUser) defaultUser);
        } else if (request.getRole() == Role.MODERATOR) {
            return toModeratorUser(request, (ModeratorUser) defaultUser);
        }
        return toSimpleUser(request, (SimpleUser) defaultUser);
    }

    abstract SimpleUser toSimpleUser(UserDto request);

    @Mapping(target = "password", ignore = true)
    abstract SimpleUser toSimpleUser(UserDto request, @MappingTarget SimpleUser user);

    abstract ModeratorUser toModeratorUser(UserDto request);

    @Mapping(target = "password", ignore = true)
    abstract ModeratorUser toModeratorUser(UserDto request, @MappingTarget ModeratorUser user);

    abstract AdminUser toAdminUser(UserDto request);

    @Mapping(target = "password", ignore = true)
    abstract AdminUser toAdminUser(UserDto request, @MappingTarget AdminUser user);

    public UserDto toUserDTO(DefaultUser user) {
        if (user instanceof SimpleUser) {
            return UserDTOFromSimpleUser((SimpleUser) user);
        } else if (user instanceof ModeratorUser) {
            return UserDTOFromModeratorUser((ModeratorUser) user);
        }
        return UserDTOFromDefaultUser(user);

    }

    abstract UserDto UserDTOFromDefaultUser(DefaultUser defaultUser);

    abstract UserDto UserDTOFromModeratorUser(ModeratorUser defaultUser);

    abstract UserDto UserDTOFromSimpleUser(SimpleUser defaultUser);


}
