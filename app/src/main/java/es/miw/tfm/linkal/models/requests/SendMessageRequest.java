package es.miw.tfm.linkal.models.requests;

public class SendMessageRequest {
    private String text;

    public SendMessageRequest(String text) {
        this.text = text;
    }

    public String getText()             { return text; }
    public void   setText(String text)  { this.text = text; }
}
