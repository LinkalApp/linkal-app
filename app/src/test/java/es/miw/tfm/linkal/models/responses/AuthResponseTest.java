package es.miw.tfm.linkal.models.responses;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthResponseTest {

    @Test
    public void constructorWithArgs_getToken_returnsCorrectValue() {
        AuthResponse response = new AuthResponse("jwt-token-abc", "BUSINESS", "user@test.com", "157fe5e4-478a-42cb-88cb-fa094b5c1a31");
        assertEquals("jwt-token-abc", response.getToken());
    }

    @Test
    public void constructorWithArgs_getRole_returnsCorrectValue() {
        AuthResponse response = new AuthResponse("jwt-token-abc", "BUSINESS", "user@test.com", "0d7fe5e4-478a-42cb-89cb-fa094b5c1a54");
        assertEquals("BUSINESS", response.getRole());
    }

    @Test
    public void constructorWithArgs_getEmail_returnsCorrectValue() {
        AuthResponse response = new AuthResponse("jwt-token-abc", "BUSINESS", "user@test.com", "24cfe5e4-478a-42cb-88cb-fa094b5c1a72");
        assertEquals("user@test.com", response.getEmail());
    }

    @Test
    public void constructorWithArgs_influencerRole_returnsInfluencer() {
        AuthResponse response = new AuthResponse("jwt-inf", "INFLUENCER", "inf@linkal.es", "e5cfe5e4-478a-42cb-88cb-fa094b5c4a83");
        assertEquals("INFLUENCER", response.getRole());
    }

    @Test
    public void constructorWithArgs_getId_returnsCorrectValue() {
        AuthResponse response = new AuthResponse("jwt-token-abc", "BUSINESS", "user@test.com", "24cfe5e4-478a-42cb-88cb-fa094b5c1a72");
        assertEquals("24cfe5e4-478a-42cb-88cb-fa094b5c1a72", response.getId());
    }

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        AuthResponse response = new AuthResponse();
        assertNull(response.getToken());
        assertNull(response.getRole());
        assertNull(response.getEmail());
        assertNull(response.getId());
    }
}
