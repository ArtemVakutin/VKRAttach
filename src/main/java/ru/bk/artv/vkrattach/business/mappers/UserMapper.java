package ru.bk.artv.vkrattach.business.mappers;

import org.mapstruct.Mapper;
import ru.bk.artv.vkrattach.domain.Role;
import ru.bk.artv.vkrattach.domain.dto.UserToClientDTO;
import ru.bk.artv.vkrattach.domain.dto.UserToPatchDTO;
import ru.bk.artv.vkrattach.domain.user.AdminUser;
import ru.bk.artv.vkrattach.domain.user.DefaultUser;
import ru.bk.artv.vkrattach.domain.user.ModeratorUser;
import ru.bk.artv.vkrattach.domain.user.SimpleUser;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public DefaultUser toDefaultUser(UserToPatchDTO request){
        if (request.getRole() == Role.ADMIN) {
            return toAdminUser(request);
        } else if (request.getRole()== Role.MODERATOR) {
            return toModeratorUser(request);
        }
        return toSimpleUser(request);
    }

    abstract SimpleUser toSimpleUser(UserToPatchDTO request);
    abstract ModeratorUser toModeratorUser(UserToPatchDTO request);
    abstract AdminUser toAdminUser(UserToPatchDTO request);

    public UserToClientDTO toUserDTO(DefaultUser user) {
        if (user instanceof SimpleUser){
            return UserDTOFromSimpleUser((SimpleUser) user);
        }
        return UserDTOFromDefaultUser(user);

    }

    abstract UserToClientDTO UserDTOFromDefaultUser(DefaultUser defaultUser);
    abstract UserToClientDTO UserDTOFromSimpleUser(SimpleUser defaultUser);


}
