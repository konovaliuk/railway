package com.liashenko.app.controller.utils;

import com.liashenko.app.controller.utils.exceptions.ValidationException;

import java.util.Arrays;

import static com.liashenko.app.controller.utils.Asserts.*;

public abstract class Validator {
    //make it in properties
    //try to use chain pattern
    //try to use checking patterns
    public static final int FIRST_NAME_MAX_LENGTH = 255;
    public static final int FIRST_NAME_MIN_LENGTH = 1;

    public static final int LAST_NAME_MAX_LENGTH = 255;
    public static final int LAST_NAME_MIN_LENGTH = 1;

    public static final int EMAIL_MAX_LENGTH = 255;
    public static final int EMAIL_MIN_LENGTH = 1;

    public static final int PASS_MAX_LENGTH = 255;
    public static final int PASS_MIN_LENGTH = 1;

    public static String validateFirstName(String firstName){
        String validationMsg = checkStringLengthParams(firstName, FIRST_NAME_MIN_LENGTH, FIRST_NAME_MAX_LENGTH, "First name");
        if (!assertStringIsNullOrEmpty(validationMsg)) throw new ValidationException(validationMsg);
        return firstName;
    }

    public static String validateLastName(String lastName){
        String validationMsg = checkStringLengthParams(lastName, LAST_NAME_MIN_LENGTH, LAST_NAME_MAX_LENGTH, "Last name");
        if (!assertStringIsNullOrEmpty(validationMsg)) throw new ValidationException(validationMsg);
        return lastName;
    }

    public static String validateEmail(String email){
        String validationMsg = checkStringLengthParams(email, EMAIL_MIN_LENGTH, EMAIL_MAX_LENGTH, "E-mail");
        if (!assertStringIsNullOrEmpty(validationMsg)) throw new ValidationException(validationMsg);
        return email;
    }

    public static char[] checkRawPasswordsOnEquivalence(char [] pass, char[] repeated_pass){
        if (!Arrays.equals(pass, repeated_pass)) throw new ValidationException("Different passwords");
        return pass;
    }


    private static String checkStringLengthParams(String str, int minLength, int maxLength, String paramName) {
        StringBuilder sb = new StringBuilder();
        if (assertStringIsNullOrEmpty(str)) sb.append(String.format("%s is empty. ", paramName));

        if (assertStringSizeIsMore(str, FIRST_NAME_MAX_LENGTH)) {
            sb.append(String.format("%s size is more than %d symbol(s). ", paramName, maxLength));
        }

        if (assertStringSizeIsLess(str, FIRST_NAME_MIN_LENGTH)) {
            sb.append(String.format("%s size is less than %d symbol(s). ", paramName, minLength));
        }
        return sb.toString();
    }


}
