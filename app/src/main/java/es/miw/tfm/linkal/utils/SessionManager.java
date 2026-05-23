package es.miw.tfm.linkal.utils;

public class SessionManager {

    private static SessionManager instance;

    private String token;
    private String email;
    private String role;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void saveSession(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    public void clearSession() {
        this.token = null;
        this.email = null;
        this.role = null;
    }

    public String getToken() {
        return token;
    }

    public String getBearerToken() {
        return "Bearer " + token;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isLoggedIn() {
        return token != null;
    }
}
