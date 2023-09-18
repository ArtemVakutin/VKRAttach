package ru.bk.artv.vkrattach.services.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=PasswordConstraintValidator.class)
public @interface PasswordConstraint {
    String message() default "Пароль должен включать не менее 6 знаков, а также состоять только из символов " +
            "английского алфавита, цифр или специальных знаков @#$%^&+=";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
