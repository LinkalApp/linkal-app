package es.miw.tfm.linkal.models.responses;

public class AuthResponse {
    private String token;
    private String role;
    private String email;
    private String id;

    public AuthResponse() {}

    public AuthResponse(String token, String role, String email, String id) {
        this.token = token;
        this.role  = role;
        this.email = email;
        this.id    = id;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getId() { return id; }
}
