package es.miw.tfm.linkal.models.responses;

import java.util.List;

public class AdminUserResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String description;
    private Boolean verified;
    private String role;

    // Influencer-specific
    private String artisticName;
    private List<String> interests;
    private String instagram;
    private String tiktok;
    private String youtube;

    // Business-specific
    private String address;
    private String province;
    private String website;
    private String category;

    public AdminUserResponse() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDescription() { return description; }
    public Boolean getVerified() { return verified; }
    public String getRole() { return role; }
    public String getArtisticName() { return artisticName; }
    public List<String> getInterests() { return interests; }
    public String getInstagram() { return instagram; }
    public String getTiktok() { return tiktok; }
    public String getYoutube() { return youtube; }
    public String getAddress() { return address; }
    public String getProvince() { return province; }
    public String getWebsite() { return website; }
    public String getCategory() { return category; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDescription(String description) { this.description = description; }
    public void setVerified(Boolean verified) { this.verified = verified; }
    public void setRole(String role) { this.role = role; }
    public void setArtisticName(String artisticName) { this.artisticName = artisticName; }
    public void setInterests(List<String> interests) { this.interests = interests; }
    public void setInstagram(String instagram) { this.instagram = instagram; }
    public void setTiktok(String tiktok) { this.tiktok = tiktok; }
    public void setYoutube(String youtube) { this.youtube = youtube; }
    public void setAddress(String address) { this.address = address; }
    public void setProvince(String province) { this.province = province; }
    public void setWebsite(String website) { this.website = website; }
    public void setCategory(String category) { this.category = category; }
}
