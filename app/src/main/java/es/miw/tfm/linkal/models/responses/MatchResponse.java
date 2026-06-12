package es.miw.tfm.linkal.models.responses;

public class MatchResponse {
    private String id;
    private String status;
    private String createdAt;
    private String matchedAt;
    private String campaignId;
    private String influencerId;
    // Campaña
    private String  campaignTitle;
    private String  campaignDescription;
    private String  campaignObjective;
    private String  campaignRequirements;
    private String  campaignReward;
    private String  campaignStatus;
    private String  campaignCreationDate;
    // Negocio
    private String  businessName;
    private String  businessCategory;
    private String  businessDescription;
    private String  businessWebsite;
    private String  businessProvince;
    private String  businessAddress;
    private Boolean businessVerified;

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
}
