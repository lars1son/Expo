package com.validator;


import com.entities.UserEntity;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserEntity user = (UserEntity) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");

        if (userService.findByEmail(user.getEmail()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.email");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (user.getPassword().length() <6 ) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getConfirmPassword().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}