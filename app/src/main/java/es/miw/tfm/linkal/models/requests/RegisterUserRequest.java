package es.miw.tfm.linkal.models.requests;

public class RegisterUserRequest {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String description;
    private String role;

    public RegisterUserRequest(String name, String email, String password,
                               String phoneNumber, String description, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.role = role;
    }

    public String getName()        { return name; }
    public String getEmail()       { return email; }
    public String getPassword()    { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDescription() { return description; }
    public String getRole()        { return role; }
}
