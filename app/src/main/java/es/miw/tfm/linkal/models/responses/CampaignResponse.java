package es.miw.tfm.linkal.models.responses;

public class CampaignResponse {
    private String id;
    private String title;
    private String description;
    private String requirements;
    private String reward;
    private String objective;
    private String status;
    private String creationDate;
    private String businessId;
    // Datos del negocio (solo en para el influencer)
    private String businessName;
    private String businessCategory;
    private String businessDescription;
    private String businessWebsite;
    private String businessProvince;
    private String businessAddress;
    private Boolean businessVerified;
    private Double businessAverageRating;

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() { return description;}
    public String getRequirements() { return requirements; }
    public String getReward() { return reward;}
    public String getObjective() { return objective;}
    public String getStatus() { return status; }
    public String getCreationDate() { return creationDate; }
    public String getBusinessId() { return businessId; }
    public String getBusinessName() { return businessName; }
    public String getBusinessCategory() { return businessCategory; }
    public String getBusinessDescription() { return businessDescription; }
    public String getBusinessWebsite() { return businessWebsite; }
    public String getBusinessProvince() { return businessProvince; }
    public String getBusinessAddress() { return businessAddress; }
    public Boolean getBusinessVerified() { return businessVerified; }
    public Double getBusinessAverageRating() { return businessAverageRating; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public void setReward(String reward) { this.reward = reward; }
    public void setObjective(String objective) { this.objective = objective; }
    public void setStatus(String status) { this.status = status; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }
    public void setBusinessId(String businessId) { this.businessId = businessId; }

    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public void setBusinessCategory(String businessCategory) { this.businessCategory = businessCategory; }
    public void setBusinessDescription(String businessDescription) { this.businessDescription = businessDescription; }
    public void setBusinessWebsite(String businessWebsite) { this.businessWebsite = businessWebsite; }
    public void setBusinessProvince(String businessProvince) { this.businessProvince = businessProvince; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }
    public void setBusinessVerified(Boolean businessVerified) { this.businessVerified = businessVerified; }
    public void setBusinessAverageRating(Double businessAverageRating) { this.businessAverageRating = businessAverageRating; }
}
