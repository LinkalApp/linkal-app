package es.miw.tfm.linkal.models.requests;

import org.junit.Test;

import static org.junit.Assert.*;

public class ResetPasswordRequestTest {

    @Test
    public void getEmail_returnsCorrectValue() {
        ResetPasswordRequest request = new ResetPasswordRequest("user@test.com", "123456", "newPass1");
        assertEquals("user@test.com", request.getEmail());
    }

    @Test
    public void getCode_returnsCorrectValue() {
        ResetPasswordRequest request = new ResetPasswordRequest("user@test.com", "123456", "newPass1");
        assertEquals("123456", request.getCode());
    }

    @Test
    public void getNewPassword_returnsCorrectValue() {
        ResetPasswordRequest request = new ResetPasswordRequest("user@test.com", "123456", "newPass1");
        assertEquals("newPass1", request.getNewPassword());
    }

    @Test
    public void constructor_withNullEmail_returnsNull() {
        ResetPasswordRequest request = new ResetPasswordRequest(null, "123456", "newPass1");
        assertNull(request.getEmail());
    }

    @Test
    public void constructor_withNullCode_returnsNull() {
        ResetPasswordRequest request = new ResetPasswordRequest("user@test.com", null, "newPass1");
        assertNull(request.getCode());
    }

    @Test
    public void constructor_withNullPassword_returnsNull() {
        ResetPasswordRequest request = new ResetPasswordRequest("user@test.com", "123456", null);
        assertNull(request.getNewPassword());
    }
}
