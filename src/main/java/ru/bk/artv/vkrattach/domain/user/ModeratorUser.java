package ru.bk.artv.vkrattach.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;


@Data
@Entity
@DiscriminatorValue("MODERATOR")
public class ModeratorUser extends DefaultUser{
    @Column(name = "user_department")
    private String department;

}
