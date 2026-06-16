package es.miw.tfm.linkal.models.responses;

public class ChatResponse {
    private String id;
    private String name;
    private String matchId;
    private String campaignId;
    private String displayName;
    private String campaignTitle;
    private String lastMessage;
    private String lastMessageAt;

    public String getId() { return id;}
    public String getMatchId() { return matchId; }
    public String getCampaignId() { return campaignId; }
    public String getDisplayName() { return displayName; }
    public String getCampaignTitle() { return campaignTitle; }
    public String getLastMessage() { return lastMessage; }
    public String getLastMessageAt() { return lastMessageAt; }

    public void setId(String id) { this.id = id; }
    public void setMatchId(String matchId) { this.matchId = matchId; }
    public void setCampaignId(String campaignId) { this.campaignId = campaignId; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setCampaignTitle(String campaignTitle) { this.campaignTitle = campaignTitle; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public void setLastMessageAt(String lastMessageAt) { this.lastMessageAt = lastMessageAt; }
}
