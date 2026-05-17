package es.miw.tfm.linkal.models.requests;

import java.util.List;

public class RegisterInfluencerRequest extends RegisterUserRequest {

    private String artisticName;
    private List<String> interests;
    private String instagram;
    private String tiktok;
    private String youtube;

    public RegisterInfluencerRequest(String name, String email, String password,
                                     String phoneNumber, String description,
                                     String artisticName, List<String> interests,
                                     String instagram, String tiktok, String youtube) {
        super(name, email, password, phoneNumber, description, "INFLUENCER");
        this.artisticName = artisticName;
        this.interests    = interests;
        this.instagram    = instagram;
        this.tiktok       = tiktok;
        this.youtube      = youtube;
    }

    public String getArtisticName()    { return artisticName; }
    public List<String> getInterests() { return interests; }
    public String getInstagram()       { return instagram; }
    public String getTiktok()          { return tiktok; }
    public String getYoutube()         { return youtube; }
}
