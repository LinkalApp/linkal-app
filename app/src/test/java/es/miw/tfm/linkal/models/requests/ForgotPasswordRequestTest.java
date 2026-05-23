package es.miw.tfm.linkal.models.requests;

import org.junit.Test;

import static org.junit.Assert.*;

public class ForgotPasswordRequestTest {

    @Test
    public void getEmail_returnsCorrectValue() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("user@test.com");
        assertEquals("user@test.com", request.getEmail());
    }

    @Test
    public void getEmail_withDifferentEmail_returnsCorrectValue() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("influencer@linkal.es");
        assertEquals("influencer@linkal.es", request.getEmail());
    }

    @Test
    public void constructor_withNullEmail_returnsNull() {
        ForgotPasswordRequest request = new ForgotPasswordRequest(null);
        assertNull(request.getEmail());
    }
}