package es.miw.tfm.linkal.models.requests;

public class CreateCampaignRequest {
    private String title;
    private String description;
    private String requirements;
    private String reward;
    private String objective;

    public CreateCampaignRequest(String title, String description, String requirements, String reward, String objective) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.reward = reward;
        this.objective = objective;
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
}
