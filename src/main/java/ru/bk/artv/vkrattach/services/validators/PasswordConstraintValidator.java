package ru.bk.artv.vkrattach.services.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, String> {

    final static String PATTERN = "(?=.*[0-9a-zA-Z@#$%^&+=]).{6,}";

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.matches(PATTERN);
    }
}