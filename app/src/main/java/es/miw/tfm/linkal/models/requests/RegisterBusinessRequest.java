package es.miw.tfm.linkal.models.requests;

public class RegisterBusinessRequest extends RegisterUserRequest {

    private String address;
    private String province;
    private String website;
    private String category;

    public RegisterBusinessRequest(String name, String email, String password,
                                   String phoneNumber, String description,
                                   String address, String province,
                                   String website, String category) {
        super(name, email, password, phoneNumber, description, "BUSINESS");
        this.address  = address;
        this.province = province;
        this.website  = website;
        this.category = category;
    }

    public String getAddress()  { return address; }
    public String getProvince() { return province; }
    public String getWebsite()  { return website; }
    public String getCategory() { return category; }
}


