package es.miw.tfm.linkal.models.requests;

public class UpdateBusinessRequest {
    private String name;
    private String phoneNumber;
    private String description;
    private String address;
    private String province;
    private String website;

    public UpdateBusinessRequest() {}

    public UpdateBusinessRequest(String name, String phoneNumber, String description, String address, String province, String website) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.address = address;
        this.province = province;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getProvince() {
        return province;
    }

    public String getWebsite() {
        return website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
