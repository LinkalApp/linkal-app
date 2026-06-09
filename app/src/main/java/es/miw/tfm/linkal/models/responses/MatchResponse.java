package es.miw.tfm.linkal.models.responses;

public class MatchResponse {
    private String id;
    private String status;
    private String createdAt;
    private String matchedAt;
    private String campaignId;
    private String influencerId;

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

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setMatchedAt(String matchedAt) {
        this.matchedAt = matchedAt;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }
}
