package es.miw.tfm.linkal.models.requests;

public class UpdateCampaignRequest {
    private String title;
    private String description;
    private String requirements;
    private String reward;
    private String objective;
    private String status;

    public UpdateCampaignRequest() {}

    public UpdateCampaignRequest(String title, String description, String requirements, String reward, String objective, String status) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.reward = reward;
        this.objective = objective;
        this.status = status;
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
}
