package com.exampleepaam.restaurant.validator;

import com.exampleepaam.restaurant.constant.ErrorAttributeConstants;
import com.exampleepaam.restaurant.model.dto.UserCreationDto;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

import static com.exampleepaam.restaurant.constant.ErrorAttributeConstants.*;

/*
 * Class with static validation methods for UserCreationDTO model
 */
public class UserValidator {
    private static final int MAX_PASSWORD_LENGTH = 25;
    private static final int MIN_PASSWORD_LENGTH = 8;


    private UserValidator() {
    }

    /**
     * Validates a user
     *
     * @param user UserCreationDto that is validated
     * @return Map with error names and error keys if dish is invalid
     */
    public static Map<String, String> isUserValid(UserCreationDto user) {
        Map<String, String> viewAttributes = new HashMap<>();

        if (!UserValidator.isNameValid(user.getName())) {
            viewAttributes.put(ERROR_ATTRIBUTE_NAME, ErrorAttributeConstants.NAME_NOT_VALID);
        }

        if (!UserValidator.isEmailValid(user.getEmail())) {
            viewAttributes.put(ERROR_ATTRIBUTE_EMAIL, ErrorAttributeConstants.EMAIL_NOT_VALID);
        }

        if (!UserValidator.isPasswordValid(user.getPassword())) {
            viewAttributes.put(ERROR_ATTRIBUTE_PASSWORD, ErrorAttributeConstants.PASSWORD_NOT_VALID);
        }
        if (!UserValidator.ifPasswordsMatch(user.getPassword(), user.getMatchingPassword())) {
            viewAttributes.put(ERROR_ATTRIBUTE_PASSWORD, ErrorAttributeConstants.PASSWORDS_DO_NOT_MATCH);
        }

        return viewAttributes;
    }


    public static boolean isPasswordValid(String password) {
        if (password == null || password.isBlank()) return false;
        return password.length() >= MIN_PASSWORD_LENGTH && password.length() <= MAX_PASSWORD_LENGTH;
    }

    public static boolean ifPasswordsMatch(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

    public static boolean isEmailValid(String email) {
        return email != null && EmailValidator.getInstance().isValid(email);
    }

    public static boolean isNameValid(String name) {
        return !name.isBlank();
    }

    public static boolean isPasswordCorrect(String passwordInput, String realPassword) {
        return BCrypt.checkpw(passwordInput, realPassword);
    }
}
