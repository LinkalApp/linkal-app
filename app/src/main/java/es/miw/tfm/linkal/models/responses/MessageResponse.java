package es.miw.tfm.linkal.models.responses;

public class MessageResponse {
    private String id;
    private String text;
    private String sentAt;
    private String chatId;
    private String senderId;

    public String getId() { return id; }
    public String getText() { return text; }
    public String getSentAt() { return sentAt; }
    public String getChatId() { return chatId; }
    public String getSenderId() { return senderId; }

    public void setId(String id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }
    public void setChatId(String chatId) { this.chatId = chatId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
}
