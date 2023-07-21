package com.drawhale.authenticationservice.application.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.regex.Pattern;

/***
 * Password Validation
 *
 */
@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final int MIN_SIZE = 8;
    private static final int MAX_SIZE = 50;
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{" + MIN_SIZE + "," + MAX_SIZE + "}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(Password constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        boolean isValidPassword = pattern.matcher(password).matches();
        if (!isValidPassword) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            MessageFormat.format("{0}자 이상의 {1}자 이하의 숫자, 영문자, 특수문자를 포함한 비밀번호를 입력해주세요", MIN_SIZE, MAX_SIZE))
                    .addConstraintViolation();
        }
        return isValidPassword;
    }
}
