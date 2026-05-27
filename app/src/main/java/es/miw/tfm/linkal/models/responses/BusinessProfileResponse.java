package es.miw.tfm.linkal.models.responses;

public class BusinessProfileResponse {
    private String name;
    private String email;
    private String phoneNumber;
    private String description;
    private String address;
    private String province;
    private String website;
    private String category;
    private Boolean verified;
    private Double  averageRating;
    public BusinessProfileResponse() {}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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

    public String getCategory() {
        return category;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAverageRating() {
        return averageRating;
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
