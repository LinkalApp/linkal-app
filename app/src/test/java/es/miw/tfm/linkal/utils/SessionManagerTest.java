package es.miw.tfm.linkal.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SessionManagerTest {

    private SessionManager sessionManager;

    @Before
    public void setUp() {
        sessionManager = SessionManager.getInstance();
        sessionManager.clearSession();
    }

    // saveSession / getters --------------------------------------------------------------------
    @Test
    public void saveSession_shouldStoreToken() {
        sessionManager.saveSession("my-token", "user@test.com", "INFLUENCER");
        assertEquals("my-token", sessionManager.getToken());
    }

    @Test
    public void saveSession_shouldStoreEmail() {
        sessionManager.saveSession("my-token", "user@test.com", "INFLUENCER");
        assertEquals("user@test.com", sessionManager.getEmail());
    }

    @Test
    public void saveSession_shouldStoreRole() {
        sessionManager.saveSession("my-token", "user@test.com", "INFLUENCER");
        assertEquals("INFLUENCER", sessionManager.getRole());
    }

    @Test
    public void getBearerToken_shouldPrependBearer() {
        sessionManager.saveSession("abc123", "user@test.com", "INFLUENCER");
        assertEquals("Bearer abc123", sessionManager.getBearerToken());
    }

    // isLoggedIn -------------------------------------------------------------------------

    @Test
    public void isLoggedIn_shouldReturnTrueWhenTokenIsSet() {
        sessionManager.saveSession("token", "user@test.com", "INFLUENCER");
        assertTrue(sessionManager.isLoggedIn());
    }

    @Test
    public void isLoggedIn_shouldReturnFalseWhenNoToken() {
        assertFalse(sessionManager.isLoggedIn());
    }

    // clearSession --------------------------------------------------------------------------------

    @Test
    public void clearSession_shouldNullifyToken() {
        sessionManager.saveSession("token", "user@test.com", "INFLUENCER");
        sessionManager.clearSession();
        assertNull(sessionManager.getToken());
    }

    @Test
    public void clearSession_shouldNullifyEmail() {
        sessionManager.saveSession("token", "user@test.com", "INFLUENCER");
        sessionManager.clearSession();
        assertNull(sessionManager.getEmail());
    }

    @Test
    public void clearSession_shouldNullifyRole() {
        sessionManager.saveSession("token", "user@test.com", "INFLUENCER");
        sessionManager.clearSession();
        assertNull(sessionManager.getRole());
    }

    @Test
    public void clearSession_shouldSetIsLoggedInToFalse() {
        sessionManager.saveSession("token", "user@test.com", "INFLUENCER");
        sessionManager.clearSession();
        assertFalse(sessionManager.isLoggedIn());
    }

    // Singleton -----------------------------------------------------------------------

    @Test
    public void getInstance_shouldReturnSameInstance() {
        SessionManager a = SessionManager.getInstance();
        SessionManager b = SessionManager.getInstance();
        assertSame(a, b);
    }
}
