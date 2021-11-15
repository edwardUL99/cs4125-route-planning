package ie.ul.routeplanning.users;

import ie.ul.routeplanning.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * This class is used to validate users in the system
 */
@Component
public class UserValidator implements Validator {
    /**
     * The user service for the system
     */
    private UserService userService;

    /**
     * Create a UserValidator with the provided user service dependency
     * @param userService the user service for accessing users
     */
    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    /**
     * Determines if the validator supports the provided class
     * @param clazz the class to check
     * @return true if supported, false if not
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    /**
     * Validates the user
     * @param target the target user
     * @param errors the errors from validation
     */
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        String username = user.getUsername();
        int usernameLength = username.length();
        if (usernameLength < 6 || usernameLength > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }

        if (userService.findByUsername(username) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        String email = user.getEmail();

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");

        if (!email.matches(
                "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            errors.rejectValue("email", "email.Invalid");
        }

        if (userService.findByEmail(email) != null) {
            errors.rejectValue("email", "Duplicate.userForm.email");
        }

        String password = user.getPassword();
        int passwordLength = password.length();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (passwordLength < 8 || passwordLength > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(password)) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}
