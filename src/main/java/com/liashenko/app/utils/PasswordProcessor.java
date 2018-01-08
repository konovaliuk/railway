package com.liashenko.app.utils;

import com.liashenko.app.utils.exceptions.PasswordProcessorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static com.liashenko.app.controller.utils.Asserts.assertIsNull;
import static com.liashenko.app.controller.utils.Asserts.assertStringIsNullOrEmpty;

public abstract class PasswordProcessor {
    private static final Logger classLogger = LogManager.getLogger(PasswordProcessor.class);

    public static byte[] generateSalt(int passwordLength) {
        try {
            SecureRandom sr = SecureRandom.getInstance(AppProperties.getSaltGenerationAlg());
            byte[] salt = new byte[passwordLength * 2];
            sr.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            throw new PasswordProcessorException(ex.getMessage());
        }
    }

    public static byte[] getEncryptedPass(char[] password, byte[] salt) {
        if (salt == null || password == null) throw new PasswordProcessorException("Wrong parameters");
        int iterations = AppProperties.getPassAlgIterationValue();
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(AppProperties.getPassGenerationAlg());
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            classLogger.error(ex);
            throw new PasswordProcessorException(ex.getMessage());
        }
    }

    public static boolean checkPassword(char[] originalPassword, byte[] storedPassword, int iterations, byte[] salt,
                                        String saltGenerationAlg) {
        if (assertIsNull(originalPassword) || assertIsNull(storedPassword)
                || assertIsNull(salt) || assertStringIsNullOrEmpty(saltGenerationAlg)) {
            throw new PasswordProcessorException("Wrong parameters");
        }
        try {
            PBEKeySpec spec = new PBEKeySpec(originalPassword, salt, iterations, storedPassword.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(saltGenerationAlg);
            byte[] testHash = skf.generateSecret(spec).getEncoded();
            return Arrays.equals(storedPassword, testHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            classLogger.error(ex);
            throw new PasswordProcessorException(ex.getMessage());
        }
    }
}
