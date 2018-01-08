package com.liashenko.app.controller.utils;

import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.utils.AppProperties;

import java.util.Arrays;

import static com.liashenko.app.controller.utils.Asserts.*;

//try to use chain pattern
public abstract class Validator {

    public static String validateFirstName(String firstName) {
        String validationMsg = checkStringLengthParams(firstName, AppProperties.getFirstNameMinLength(),
                AppProperties.getFirstNameMaxLength(), "First name");
        if (!assertStringIsNullOrEmpty(validationMsg)) throw new ValidationException(validationMsg);
        return firstName;
    }

    public static String validateLastName(String lastName) {
        String validationMsg = checkStringLengthParams(lastName, AppProperties.getLastNameMinLength(),
                AppProperties.getLastNameMaxLength(), "Last name");
        if (!assertStringIsNullOrEmpty(validationMsg)) throw new ValidationException(validationMsg);
        return lastName;
    }

    public static String validateEmail(String email) {
        String validationMsg = checkStringLengthParams(email, AppProperties.getPassMinLength(),
                AppProperties.getPassMaxLength(), "E-mail");
        if (!assertStringIsNullOrEmpty(validationMsg)) throw new ValidationException(validationMsg);
        return email;
    }

    public static char[] checkRawPasswordsOnEquivalence(char[] pass, char[] repeated_pass) {
        if (!Arrays.equals(pass, repeated_pass)) throw new ValidationException("Different passwords");
        return pass;
    }

    private static String checkStringLengthParams(String str, int minLength, int maxLength, String paramName) {
        StringBuilder sb = new StringBuilder();
        if (assertStringIsNullOrEmpty(str)) sb.append(String.format("%s is empty. ", paramName));

        if (assertStringSizeIsMore(str, AppProperties.getFirstNameMaxLength())) {
            sb.append(String.format("%s size is more than %d symbol(s). ", paramName, maxLength));
        }

        if (assertStringSizeIsLess(str, AppProperties.getFirstNameMinLength())) {
            sb.append(String.format("%s size is less than %d symbol(s). ", paramName, minLength));
        }
        return sb.toString();
    }
}
