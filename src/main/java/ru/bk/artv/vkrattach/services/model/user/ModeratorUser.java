package ru.bk.artv.vkrattach.services.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Объект данных User с правами доступа Moderator
 * <p>
 * Обязательные поля - login (в родителе). и department (собственно с какой кафедры)
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("MODERATOR")
public class ModeratorUser extends DefaultUser{
    @Column(name = "user_department")
    private String department;

}
