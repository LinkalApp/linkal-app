package es.miw.tfm.linkal.models.responses;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthResponseTest {

    @Test
    public void constructorWithArgs_getToken_returnsCorrectValue() {
        AuthResponse response = new AuthResponse("jwt-token-abc", "BUSINESS", "user@test.com");
        assertEquals("jwt-token-abc", response.getToken());
    }

    @Test
    public void constructorWithArgs_getRole_returnsCorrectValue() {
        AuthResponse response = new AuthResponse("jwt-token-abc", "BUSINESS", "user@test.com");
        assertEquals("BUSINESS", response.getRole());
    }

    @Test
    public void constructorWithArgs_getEmail_returnsCorrectValue() {
        AuthResponse response = new AuthResponse("jwt-token-abc", "BUSINESS", "user@test.com");
        assertEquals("user@test.com", response.getEmail());
    }

    @Test
    public void constructorWithArgs_influencerRole_returnsInfluencer() {
        AuthResponse response = new AuthResponse("jwt-inf", "INFLUENCER", "inf@linkal.es");
        assertEquals("INFLUENCER", response.getRole());
    }

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        AuthResponse response = new AuthResponse();
        assertNull(response.getToken());
        assertNull(response.getRole());
        assertNull(response.getEmail());
    }
}
