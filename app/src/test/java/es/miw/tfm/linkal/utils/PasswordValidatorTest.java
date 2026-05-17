package es.miw.tfm.linkal.utils;

import org.junit.Test;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

public class PasswordValidatorTest {

    // isValid ---------------------------------------------------

    @Test
    public void isValid_withAllRequirements_returnsTrue() {
        assertTrue(PasswordValidator.isValid("Secure1."));
        assertTrue(PasswordValidator.isValid("Pass1word!"));
        assertTrue(PasswordValidator.isValid("Hello123,World"));
        assertTrue(PasswordValidator.isValid("Abc@1234"));
        assertTrue(PasswordValidator.isValid("MyP4ss_word"));
    }

    @Test
    public void isValid_withNull_returnsFalse() {
        assertFalse(PasswordValidator.isValid(null));
    }

    @Test
    public void isValid_withEmpty_returnsFalse() {
        assertFalse(PasswordValidator.isValid(""));
    }

    @Test
    public void isValid_withNoUpperCase_returnsFalse() {
        assertFalse(PasswordValidator.isValid("secure1."));
    }

    @Test
    public void isValid_withNoLowerCase_returnsFalse() {
        assertFalse(PasswordValidator.isValid("SECURE1."));
    }

    @Test
    public void isValid_withNoDigit_returnsFalse() {
        assertFalse(PasswordValidator.isValid("SecurePass."));
    }

    @Test
    public void isValid_withNoSpecialChar_returnsFalse() {
        assertFalse(PasswordValidator.isValid("Secure123"));
    }

    @Test
    public void isValid_onlyLetters_returnsFalse() {
        assertFalse(PasswordValidator.isValid("SecurePass"));
    }

    @Test
    public void isValid_onlyNumbers_returnsFalse() {
        assertFalse(PasswordValidator.isValid("12345678"));
    }

    //  hasUpperCase ----------------------------------------------

    @Test
    public void hasUpperCase_withUpperCase_returnsTrue() {
        assertTrue(PasswordValidator.hasUpperCase("Hello"));
        assertTrue(PasswordValidator.hasUpperCase("A"));
    }

    @Test
    public void hasUpperCase_withNoUpperCase_returnsFalse() {
        assertFalse(PasswordValidator.hasUpperCase("hello"));
        assertFalse(PasswordValidator.hasUpperCase("123."));
    }

    @Test
    public void hasUpperCase_withNull_returnsFalse() {
        assertFalse(PasswordValidator.hasUpperCase(null));
    }

    // hasLowerCase ------------------------------------------------

    @Test
    public void hasLowerCase_withLowerCase_returnsTrue() {
        assertTrue(PasswordValidator.hasLowerCase("hello"));
        assertTrue(PasswordValidator.hasLowerCase("a"));
    }

    @Test
    public void hasLowerCase_withNoLowerCase_returnsFalse() {
        assertFalse(PasswordValidator.hasLowerCase("HELLO"));
        assertFalse(PasswordValidator.hasLowerCase("123."));
    }

    @Test
    public void hasLowerCase_withNull_returnsFalse() {
        assertFalse(PasswordValidator.hasLowerCase(null));
    }

    // hasDigit ------------------------------------------------

    @Test
    public void hasDigit_withDigit_returnsTrue() {
        assertTrue(PasswordValidator.hasDigit("pass1"));
        assertTrue(PasswordValidator.hasDigit("0"));
    }

    @Test
    public void hasDigit_withNoDigit_returnsFalse() {
        assertFalse(PasswordValidator.hasDigit("Pass.word"));
        assertFalse(PasswordValidator.hasDigit("abcABC"));
    }

    @Test
    public void hasDigit_withNull_returnsFalse() {
        assertFalse(PasswordValidator.hasDigit(null));
    }

    // hasSpecialChar -----------------------------------------------

    @Test
    public void hasSpecialChar_withDot_returnsTrue() {
        assertTrue(PasswordValidator.hasSpecialChar("Pass1."));
    }

    @Test
    public void hasSpecialChar_withComma_returnsTrue() {
        assertTrue(PasswordValidator.hasSpecialChar("Pass1,"));
    }

    @Test
    public void hasSpecialChar_withUnderscore_returnsTrue() {
        assertTrue(PasswordValidator.hasSpecialChar("Pass1_"));
    }

    @Test
    public void hasSpecialChar_withAt_returnsTrue() {
        assertTrue(PasswordValidator.hasSpecialChar("Pass1@"));
    }

    @Test
    public void hasSpecialChar_withHyphen_returnsTrue() {
        assertTrue(PasswordValidator.hasSpecialChar("Pass1-"));
    }

    @Test
    public void hasSpecialChar_withNoSpecialChar_returnsFalse() {
        assertFalse(PasswordValidator.hasSpecialChar("Pass1word"));
        assertFalse(PasswordValidator.hasSpecialChar("ABCabc123"));
    }

    @Test
    public void hasSpecialChar_withNull_returnsFalse() {
        assertFalse(PasswordValidator.hasSpecialChar(null));
    }

    // getValidationError ----------------------------------------------

    @Test
    public void getValidationError_withValidPassword_returnsNull() {
        assertNull(PasswordValidator.getValidationError("Secure1."));
        assertNull(PasswordValidator.getValidationError("MyP4ss_word"));
    }

    @Test
    public void getValidationError_withNull_returnsObligatoryMessage() {
        String error = PasswordValidator.getValidationError(null);
        assertNotNull(error);
        assertTrue(error.contains("obligatoria"));
    }

    @Test
    public void getValidationError_withEmpty_returnsObligatoryMessage() {
        String error = PasswordValidator.getValidationError("");
        assertNotNull(error);
        assertTrue(error.contains("obligatoria"));
    }

    @Test
    public void getValidationError_withNoUpperCase_mentionsUpperCase() {
        String error = PasswordValidator.getValidationError("secure1.");
        assertNotNull(error);
        assertTrue(error.contains("mayúscula"));
    }

    @Test
    public void getValidationError_withNoLowerCase_mentionsLowerCase() {
        String error = PasswordValidator.getValidationError("SECURE1.");
        assertNotNull(error);
        assertTrue(error.contains("minúscula"));
    }

    @Test
    public void getValidationError_withNoDigit_mentionsNumber() {
        String error = PasswordValidator.getValidationError("SecurePass.");
        assertNotNull(error);
        assertTrue(error.contains("número"));
    }

    @Test
    public void getValidationError_withNoSpecialChar_mentionsSpecialChar() {
        String error = PasswordValidator.getValidationError("SecurePass1");
        assertNotNull(error);
        assertTrue(error.contains("especial"));
    }

    @Test
    public void getValidationError_withLessThan8Chars_mentionsLength() {
        String error = PasswordValidator.getValidationError("Abc@123");
        assertNotNull(error);
        assertTrue(error.contains("8"));
    }

    // Cobertura del constructor privado ---------------------------------

    @Test
    public void privateConstructor_isCoveredByReflection() throws Exception {
        Constructor<PasswordValidator> constructor =
                PasswordValidator.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertNotNull(constructor.newInstance());
    }

    // Branches combinados en isValid() -------------------------------------

    @Test
    public void isValid_failsOnFirstMissingRequirement_upperCase() {
        // Tiene minúscula, número y especial, pero NO mayúscula
        assertFalse(PasswordValidator.isValid("secure1."));
    }

    @Test
    public void isValid_failsOnSecondMissingRequirement_lowerCase() {
        // Tiene mayúscula, número y especial, pero NO minúscula
        assertFalse(PasswordValidator.isValid("SECURE1."));
    }

    @Test
    public void isValid_failsOnThirdMissingRequirement_digit() {
        // Tiene mayúscula, minúscula y especial, pero NO número
        assertFalse(PasswordValidator.isValid("SecurePass."));
    }

    @Test
    public void isValid_failsOnFourthMissingRequirement_specialChar() {
        // Tiene mayúscula, minúscula y número, pero NO especial
        assertFalse(PasswordValidator.isValid("SecurePass1"));
    }

    @Test
    public void isValid_failsOnFifthMissingRequirement_minLength() {
        // Tiene mayúscula, minúscula, número y especial, pero menos de 8 chars
        assertFalse(PasswordValidator.isValid("Abc@123"));
    }
}