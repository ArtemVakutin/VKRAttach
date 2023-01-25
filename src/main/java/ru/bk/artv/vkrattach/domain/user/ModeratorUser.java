package ru.bk.artv.vkrattach.domain.user;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("MODERATOR")
public class ModeratorUser extends DefaultUser{
}
