package es.miw.tfm.linkal.models.responses;

public class EvaluationResponse {
    private String  id;
    private Integer score;
    private String  valuedUserId;
    private String  matchId;

    public String getId() { return id; }
    public Integer getScore() { return score; }
    public String getValuedUserId() { return valuedUserId; }
    public String getMatchId() { return matchId; }

    public void setId(String id) { this.id = id; }
    public void setScore(Integer score) { this.score = score; }
    public void setValuedUserId(String valuedUserId) { this.valuedUserId = valuedUserId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }
}
