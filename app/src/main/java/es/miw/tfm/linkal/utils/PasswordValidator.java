package es.miw.tfm.linkal.utils;

import java.util.regex.Pattern;

/**
 * Valida que una contraseña cumpla los requisitos de seguridad:
 * al menos una mayúscula, una minúscula, un número y un carácter especial.
 * al menos 8 caracteres de longitud.
 */
public class PasswordValidator {

    private static final Pattern HAS_UPPER   = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWER   = Pattern.compile("[a-z]");
    private static final Pattern HAS_DIGIT   = Pattern.compile("\\d");
    private static final Pattern HAS_SPECIAL = Pattern.compile("[^A-Za-z\\d]");

    private PasswordValidator() {}

    /** Devuelve true si la contraseña cumple todos los requisitos. */
    public static boolean isValid(String password) {
        return password != null
                && hasUpperCase(password)
                && hasLowerCase(password)
                && hasDigit(password)
                && hasSpecialChar(password)
                && password.length() >= 8;
    }

    /** Al menos una letra mayúscula (A-Z). */
    public static boolean hasUpperCase(String password) {
        return password != null && HAS_UPPER.matcher(password).find();
    }

    /** Al menos una letra minúscula (a-z). */
    public static boolean hasLowerCase(String password) {
        return password != null && HAS_LOWER.matcher(password).find();
    }

    /** Al menos un dígito (0-9). */
    public static boolean hasDigit(String password) {
        return password != null && HAS_DIGIT.matcher(password).find();
    }

    /** Al menos un carácter especial (cualquier símbolo que no sea letra ni dígito). */
    public static boolean hasSpecialChar(String password) {
        return password != null && HAS_SPECIAL.matcher(password).find();
    }

    /**
     * Devuelve el mensaje de error del primer requisito que falla,
     * o null si la contraseña es válida.
     */
    public static String getValidationError(String password) {
        if (password == null || password.isEmpty()) {
            return "La contraseña es obligatoria";
        }
        if (!hasUpperCase(password)) {
            return "La contraseña debe contener al menos una letra mayúscula";
        }
        if (!hasLowerCase(password)) {
            return "La contraseña debe contener al menos una letra minúscula";
        }
        if (!hasDigit(password)) {
            return "La contraseña debe contener al menos un número";
        }
        if (!hasSpecialChar(password)) {
            return "La contraseña debe contener al menos un carácter especial (., -, _, @...)";
        }
        if(password.length() < 8) {
            return "La contraseña debe tener al menos 8 caracteres";
        }
        return null;
    }
}