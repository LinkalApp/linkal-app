package es.miw.tfm.linkal.models.requests;

public class ResetPasswordRequest {

    private String email;
    private String code;
    private String newPassword;

    public ResetPasswordRequest(String email, String code, String newPassword) {
        this.email       = email;
        this.code        = code;
        this.newPassword = newPassword;
    }

    public String getEmail()       { return email; }
    public String getCode()        { return code; }
    public String getNewPassword() { return newPassword; }
}
