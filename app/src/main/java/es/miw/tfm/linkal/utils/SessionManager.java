package es.miw.tfm.linkal.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREFS_NAME = "linkal_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_ID = "userId";

    private static SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SessionManager no inicializado. Llama a SessionManager.init(context) primero.");
        }
        return instance;
    }

    public void saveSession(String token, String email, String role, String userId) {
        prefs.edit()
                .putString(KEY_TOKEN,   token)
                .putString(KEY_EMAIL,   email)
                .putString(KEY_ROLE,    role)
                .putString(KEY_USER_ID, userId)
                .apply();
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    @Deprecated
    public void setUserId(String userId) {
        prefs.edit().putString(KEY_USER_ID, userId).apply();
    }

    public String getToken() { return prefs.getString(KEY_TOKEN,null); }
    public String getBearerToken() { return "Bearer " + prefs.getString(KEY_TOKEN, ""); }
    public String getEmail() { return prefs.getString(KEY_EMAIL,null); }
    public String getRole() { return prefs.getString(KEY_ROLE,null); }
    public String getUserId() { return prefs.getString(KEY_USER_ID,null); }
    public boolean isLoggedIn() { return getToken() != null; }
}
