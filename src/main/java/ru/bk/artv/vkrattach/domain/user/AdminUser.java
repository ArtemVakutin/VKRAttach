package ru.bk.artv.vkrattach.domain.user;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends DefaultUser{
}
