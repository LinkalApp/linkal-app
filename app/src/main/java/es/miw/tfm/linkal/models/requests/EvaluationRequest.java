package es.miw.tfm.linkal.models.requests;

public class EvaluationRequest {
    private Integer score;

    public EvaluationRequest() {}

    public EvaluationRequest(Integer score) {
        this.score = score;
    }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}
