package ru.bk.artv.vkrattach.services.model.user;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

/**
 * Объект данных User с правами доступа ADMIN
 * Впоследствие возможно расширение по полям, пока необходимости нет.
 */
@Entity
@DiscriminatorValue("ADMIN")
public class AdminUser extends DefaultUser{
}
