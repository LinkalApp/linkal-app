package es.miw.tfm.linkal.models.responses;

import java.util.List;

public class MatchResponse {
    private String id;
    private String status;
    private String createdAt;
    private String matchedAt;
    private String campaignId;
    private String influencerId;
    // Campaña
    private String campaignTitle;
    private String campaignDescription;
    private String campaignObjective;
    private String campaignRequirements;
    private String campaignReward;
    private String campaignStatus;
    private String campaignCreationDate;
    // Negocio
    private String businessName;
    private String businessCategory;
    private String businessDescription;
    private String businessWebsite;
    private String businessProvince;
    private String businessAddress;
    private Boolean businessVerified;
    // Influencer
    private String influencerName;
    private String influencerArtisticName;
    private String influencerDescription;
    private String influencerEmail;
    private String influencerInstagram;
    private String influencerTiktok;
    private String influencerYoutube;
    private Boolean influencerVerified;
    private List<String> influencerInterests;
    private Boolean alreadyRatedBusiness;


    public String getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public String getMatchedAt() {
        return matchedAt;
    }
    public String getCampaignId() {
        return campaignId;
    }
    public String getInfluencerId() {
        return influencerId;
    }
    public String getCampaignTitle() { return campaignTitle; }
    public String getBusinessName() { return businessName; }
    public String getCampaignDescription() { return campaignDescription; }
    public String getCampaignObjective() { return campaignObjective; }
    public String getCampaignRequirements() { return campaignRequirements; }
    public String getCampaignReward() { return campaignReward; }
    public String getCampaignStatus() { return campaignStatus; }
    public String getCampaignCreationDate() { return campaignCreationDate; }
    public String getBusinessCategory() { return businessCategory; }
    public String getBusinessDescription() { return businessDescription; }
    public String getBusinessWebsite() { return businessWebsite; }
    public String getBusinessProvince() { return businessProvince; }
    public String getBusinessAddress() { return businessAddress; }
    public Boolean getBusinessVerified() { return businessVerified; }
    public String getInfluencerName() { return influencerName; }
    public String getInfluencerArtisticName() { return influencerArtisticName; }
    public String getInfluencerDescription() { return influencerDescription; }
    public String getInfluencerEmail() { return influencerEmail; }
    public String getInfluencerInstagram() { return influencerInstagram; }
    public String getInfluencerTiktok() { return influencerTiktok; }
    public String getInfluencerYoutube() { return influencerYoutube; }
    public Boolean getInfluencerVerified() { return influencerVerified; }
    public List<String> getInfluencerInterests() { return influencerInterests; }
    public Boolean getAlreadyRatedBusiness() { return alreadyRatedBusiness; }


    public void setId(String id) { this.id = id; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setMatchedAt(String matchedAt) { this.matchedAt = matchedAt; }
    public void setCampaignId(String campaignId) { this.campaignId = campaignId; }
    public void setInfluencerId(String influencerId) { this.influencerId = influencerId; }
    public void setCampaignTitle(String campaignTitle) { this.campaignTitle = campaignTitle; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public void setCampaignDescription(String campaignDescription) { this.campaignDescription = campaignDescription; }
    public void setCampaignObjective(String campaignObjective) { this.campaignObjective = campaignObjective; }
    public void setCampaignRequirements(String campaignRequirements) { this.campaignRequirements = campaignRequirements; }
    public void setCampaignReward(String campaignReward) { this.campaignReward = campaignReward; }
    public void setCampaignStatus(String campaignStatus) { this.campaignStatus = campaignStatus; }
    public void setCampaignCreationDate(String campaignCreationDate) { this.campaignCreationDate = campaignCreationDate; }
    public void setBusinessCategory(String businessCategory) { this.businessCategory = businessCategory; }
    public void setBusinessDescription(String businessDescription) { this.businessDescription = businessDescription; }
    public void setBusinessWebsite(String businessWebsite) { this.businessWebsite = businessWebsite; }
    public void setBusinessProvince(String businessProvince) { this.businessProvince = businessProvince; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }
    public void setBusinessVerified(Boolean businessVerified) { this.businessVerified = businessVerified; }
    public void setInfluencerName(String influencerName) { this.influencerName = influencerName; }
    public void setInfluencerArtisticName(String influencerArtisticName) { this.influencerArtisticName = influencerArtisticName; }
    public void setInfluencerDescription(String influencerDescription) { this.influencerDescription = influencerDescription; }
    public void setInfluencerEmail(String influencerEmail) { this.influencerEmail = influencerEmail; }
    public void setInfluencerInstagram(String influencerInstagram) { this.influencerInstagram = influencerInstagram; }
    public void setInfluencerTiktok(String influencerTiktok) { this.influencerTiktok = influencerTiktok; }
    public void setInfluencerYoutube(String influencerYoutube) { this.influencerYoutube = influencerYoutube; }
    public void setInfluencerVerified(Boolean influencerVerified) { this.influencerVerified = influencerVerified; }
    public void setInfluencerInterests(List<String> influencerInterests) { this.influencerInterests = influencerInterests; }
    public void setAlreadyRatedBusiness(Boolean v) { this.alreadyRatedBusiness = v; }

}
