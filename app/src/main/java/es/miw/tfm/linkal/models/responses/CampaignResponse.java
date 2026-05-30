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

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getReward() {
        return reward;
    }

    public String getObjective() {
        return objective;
    }

    public String getStatus() {
        return status;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
