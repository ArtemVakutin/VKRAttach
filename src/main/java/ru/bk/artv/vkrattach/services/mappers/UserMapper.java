package ru.bk.artv.vkrattach.services.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.dto.UserToClientDTO;
import ru.bk.artv.vkrattach.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.domain.user.AdminUser;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.ModeratorUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public DefaultUser toDefaultUser(UserToPatchDTO request) {
        if (request.getRole() == Role.ADMIN) {
            return toAdminUser(request);
        } else if (request.getRole() == Role.MODERATOR) {
            return toModeratorUser(request);
        }
        return toSimpleUser(request);
    }

    public DefaultUser toDefaultUser(UserToPatchDTO request, DefaultUser defaultUser) {
        if (request.getRole() == Role.ADMIN) {
            return toAdminUser(request, (AdminUser) defaultUser);
        } else if (request.getRole() == Role.MODERATOR) {
            return toModeratorUser(request, (ModeratorUser) defaultUser);
        }
        return toSimpleUser(request, (SimpleUser) defaultUser);
    }

    abstract SimpleUser toSimpleUser(UserToPatchDTO request);

    @Mapping(target = "password", ignore = true)
    abstract SimpleUser toSimpleUser(UserToPatchDTO request, @MappingTarget SimpleUser user);

    abstract ModeratorUser toModeratorUser(UserToPatchDTO request);

    @Mapping(target = "password", ignore = true)
    abstract ModeratorUser toModeratorUser(UserToPatchDTO request, @MappingTarget ModeratorUser user);

    abstract AdminUser toAdminUser(UserToPatchDTO request);

    @Mapping(target = "password", ignore = true)
    abstract AdminUser toAdminUser(UserToPatchDTO request, @MappingTarget AdminUser user);

    public UserToClientDTO toUserDTO(DefaultUser user) {
        if (user instanceof SimpleUser) {
            return UserDTOFromSimpleUser((SimpleUser) user);
        }
        return UserDTOFromDefaultUser(user);

    }

    abstract UserToClientDTO UserDTOFromDefaultUser(DefaultUser defaultUser);

    abstract UserToClientDTO UserDTOFromSimpleUser(SimpleUser defaultUser);


}
