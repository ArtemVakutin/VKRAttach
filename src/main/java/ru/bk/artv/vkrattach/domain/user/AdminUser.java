package ru.bk.artv.vkrattach.domain.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;


@Data
@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends DefaultUser{
}
