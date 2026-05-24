package es.miw.tfm.linkal.models.requests;

import java.util.List;

public class UpdateInfluencerRequest {

    private String name;
    private String phoneNumber;
    private String description;
    private String artisticName;
    private List<String> interests;
    private String instagram;
    private String tiktok;
    private String youtube;

    public UpdateInfluencerRequest() {
    }

    public UpdateInfluencerRequest(String name, String phoneNumber, String description,
                                   String artisticName, List<String> interests,
                                   String instagram, String tiktok, String youtube) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.artisticName = artisticName;
        this.interests = interests;
        this.instagram = instagram;
        this.tiktok = tiktok;
        this.youtube = youtube;
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

    public String getArtisticName() {
        return artisticName;
    }

    public List<String> getInterests() {
        return interests;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getTiktok() {
        return tiktok;
    }

    public String getYoutube() {
        return youtube;
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

    public void setArtisticName(String artisticName) {
        this.artisticName = artisticName;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void setTiktok(String tiktok) {
        this.tiktok = tiktok;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}