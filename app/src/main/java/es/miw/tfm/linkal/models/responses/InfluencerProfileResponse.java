package es.miw.tfm.linkal.models.responses;

import java.util.List;

public class InfluencerProfileResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String description;
    private String artisticName;
    private List<String> interests;
    private String instagram;
    private String tiktok;
    private String youtube;
    private Boolean verified;
    private Double averageRating;

    public InfluencerProfileResponse() {}

    public String getId() { return id; }
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
    public String getArtisticName() {
        return artisticName;
    }
    public String getInstagram() {
        return instagram;
    }
    public List<String> getInterests() {
        return interests;
    }
    public String getTiktok() {
        return tiktok;
    }
    public String getYoutube() {
        return youtube;
    }
    public Boolean getVerified() {
        return verified;
    }
    public Double getAverageRating() {
        return averageRating;
    }

    public void setId (String id) { this.id = id; }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setArtisticName(String artisticName) {
        this.artisticName = artisticName;
    }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
    public void setTiktok(String tiktok) {
        this.tiktok = tiktok;
    }
    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
