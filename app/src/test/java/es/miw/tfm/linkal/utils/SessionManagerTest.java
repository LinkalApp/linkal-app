package es.miw.tfm.linkal.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

@RunWith(MockitoJUnitRunner.class)
public class SessionManagerTest {

    private static final String PREFS_NAME = "linkal_session";

    @Mock
    private Context mockContext;
    @Mock private SharedPreferences mockPrefs;
    @Mock private SharedPreferences.Editor mockEditor;

    @Before
    public void setUp() throws Exception {
        resetSingleton();

        // Cadena de dependencias de SharedPreferences
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSharedPreferences(eq(PREFS_NAME), eq(Context.MODE_PRIVATE)))
                .thenReturn(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);
        when(mockEditor.clear()).thenReturn(mockEditor);

        SessionManager.init(mockContext);
    }

    // Ciclo de vida ------------------------------------------------------

    @Test
    public void init_createsInstance() {
        assertNotNull(SessionManager.getInstance());
    }

    @Test
    public void getInstance_returnsSameInstance() {
        SessionManager a = SessionManager.getInstance();
        SessionManager b = SessionManager.getInstance();
        assertSame(a, b);
    }

    @Test
    public void init_doesNotReinitializeWhenCalledTwice() {
        SessionManager first = SessionManager.getInstance();
        SessionManager.init(mockContext);
        assertSame(first, SessionManager.getInstance());
    }

    @Test(expected = IllegalStateException.class)
    public void getInstance_throwsWhenNotInitialized() throws Exception {
        resetSingleton();
        SessionManager.getInstance();
    }

    // saveSession --------------------------------------------------------------

    @Test
    public void saveSession_putsTokenInEditor() {
        SessionManager.getInstance().saveSession("tok", "a@b.com", "BUSINESS", "uid-1");
        verify(mockEditor).putString("token", "tok");
    }

    @Test
    public void saveSession_putsEmailInEditor() {
        SessionManager.getInstance().saveSession("tok", "a@b.com", "BUSINESS", "uid-1");
        verify(mockEditor).putString("email", "a@b.com");
    }

    @Test
    public void saveSession_putsRoleInEditor() {
        SessionManager.getInstance().saveSession("tok", "a@b.com", "BUSINESS", "uid-1");
        verify(mockEditor).putString("role", "BUSINESS");
    }

    @Test
    public void saveSession_putsUserIdInEditor() {
        SessionManager.getInstance().saveSession("tok", "a@b.com", "BUSINESS", "uid-1");
        verify(mockEditor).putString("userId", "uid-1");
    }

    @Test
    public void saveSession_callsApply() {
        SessionManager.getInstance().saveSession("tok", "a@b.com", "BUSINESS", "uid-1");
        verify(mockEditor).apply();
    }

    // clearSession -------------------------------------------------------------

    @Test
    public void clearSession_callsEditorClear() {
        SessionManager.getInstance().clearSession();
        verify(mockEditor).clear();
    }

    @Test
    public void clearSession_callsApply() {
        SessionManager.getInstance().clearSession();
        verify(mockEditor).apply();
    }

    // setUserId (deprecated) -------------------------------------------

    @Test
    public void setUserId_putsUserIdInEditor() {
        SessionManager.getInstance().setUserId("new-uid");
        verify(mockEditor).putString("userId", "new-uid");
        verify(mockEditor).apply();
    }

    // getters --------------------------------------------------------------------

    @Test
    public void getToken_returnsValueFromPrefs() {
        when(mockPrefs.getString("token", null)).thenReturn("my-token");
        assertEquals("my-token", SessionManager.getInstance().getToken());
    }

    @Test
    public void getToken_returnsNullWhenNotSet() {
        when(mockPrefs.getString("token", null)).thenReturn(null);
        assertNull(SessionManager.getInstance().getToken());
    }

    @Test
    public void getBearerToken_returnsBearerPrefixedToken() {
        when(mockPrefs.getString("token", "")).thenReturn("abc123");
        assertEquals("Bearer abc123", SessionManager.getInstance().getBearerToken());
    }

    @Test
    public void getEmail_returnsValueFromPrefs() {
        when(mockPrefs.getString("email", null)).thenReturn("user@test.com");
        assertEquals("user@test.com", SessionManager.getInstance().getEmail());
    }

    @Test
    public void getRole_returnsValueFromPrefs() {
        when(mockPrefs.getString("role", null)).thenReturn("BUSINESS");
        assertEquals("BUSINESS", SessionManager.getInstance().getRole());
    }

    @Test
    public void getUserId_returnsValueFromPrefs() {
        when(mockPrefs.getString("userId", null)).thenReturn("uuid-xyz");
        assertEquals("uuid-xyz", SessionManager.getInstance().getUserId());
    }

    // isLoggedIn ---------------------------------------------------------------------------

    @Test
    public void isLoggedIn_returnsTrueWhenTokenNotNull() {
        when(mockPrefs.getString("token", null)).thenReturn("valid-token");
        assertTrue(SessionManager.getInstance().isLoggedIn());
    }

    @Test
    public void isLoggedIn_returnsFalseWhenTokenIsNull() {
        when(mockPrefs.getString("token", null)).thenReturn(null);
        assertFalse(SessionManager.getInstance().isLoggedIn());
    }

    // helper ------------------------------------------------------------------------

    private void resetSingleton() throws Exception {
        Field field = SessionManager.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }
}
