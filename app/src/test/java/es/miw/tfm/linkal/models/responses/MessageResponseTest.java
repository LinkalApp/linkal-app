package es.miw.tfm.linkal.models.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MessageResponseTest {
    @Test
    public void defaultConstructor_allFieldsAreNull() {
        MessageResponse r = new MessageResponse();
        assertNull(r.getId());
        assertNull(r.getText());
        assertNull(r.getSentAt());
        assertNull(r.getChatId());
        assertNull(r.getSenderId());
    }

    @Test
    public void setId_andGetId_roundTrip() {
        MessageResponse r = new MessageResponse();
        r.setId("msg-uuid-001");
        assertEquals("msg-uuid-001", r.getId());
    }

    @Test
    public void setId_toNull_returnsNull() {
        MessageResponse r = new MessageResponse();
        r.setId("msg-uuid-001");
        r.setId(null);
        assertNull(r.getId());
    }

    @Test
    public void setText_andGetText_roundTrip() {
        MessageResponse r = new MessageResponse();
        r.setText("Hola, me interesa tu perfil");
        assertEquals("Hola, me interesa tu perfil", r.getText());
    }

    @Test
    public void setText_withEmptyString_returnsEmptyString() {
        MessageResponse r = new MessageResponse();
        r.setText("");
        assertEquals("", r.getText());
    }

    @Test
    public void setText_withLongMessage_returnsFullText() {
        String longText = "a".repeat(2000);
        MessageResponse r = new MessageResponse();
        r.setText(longText);
        assertEquals(2000, r.getText().length());
    }

    @Test
    public void setText_toNull_returnsNull() {
        MessageResponse r = new MessageResponse();
        r.setText("Hola");
        r.setText(null);
        assertNull(r.getText());
    }

    @Test
    public void setSentAt_andGetSentAt_roundTrip() {
        MessageResponse r = new MessageResponse();
        r.setSentAt("2025-06-16T14:30:00");
        assertEquals("2025-06-16T14:30:00", r.getSentAt());
    }

    @Test
    public void setSentAt_toNull_returnsNull() {
        MessageResponse r = new MessageResponse();
        r.setSentAt("2025-06-16T14:30:00");
        r.setSentAt(null);
        assertNull(r.getSentAt());
    }

    @Test
    public void setChatId_andGetChatId_roundTrip() {
        MessageResponse r = new MessageResponse();
        r.setChatId("chat-uuid-001");
        assertEquals("chat-uuid-001", r.getChatId());
    }

    @Test
    public void setChatId_toNull_returnsNull() {
        MessageResponse r = new MessageResponse();
        r.setChatId("chat-uuid-001");
        r.setChatId(null);
        assertNull(r.getChatId());
    }

    @Test
    public void setSenderId_andGetSenderId_roundTrip() {
        MessageResponse r = new MessageResponse();
        r.setSenderId("user-uuid-001");
        assertEquals("user-uuid-001", r.getSenderId());
    }

    @Test
    public void setSenderId_toNull_returnsNull() {
        MessageResponse r = new MessageResponse();
        r.setSenderId("user-uuid-001");
        r.setSenderId(null);
        assertNull(r.getSenderId());
    }

    @Test
    public void twoInstances_areIndependent() {
        MessageResponse r1 = new MessageResponse();
        MessageResponse r2 = new MessageResponse();
        r1.setText("Hola!");
        r2.setText("Adios!");
        assertEquals("Hola!", r1.getText());
        assertEquals("Adios!", r2.getText());
    }

    @Test
    public void twoMessages_withSameChatId_butDifferentText_areDistinct() {
        MessageResponse r1 = new MessageResponse();
        r1.setChatId("chat-001");
        r1.setText("Primer mensaje");

        MessageResponse r2 = new MessageResponse();
        r2.setChatId("chat-001");
        r2.setText("Segundo mensaje");

        assertEquals(r1.getChatId(), r2.getChatId());
        assertNotEquals(r1.getText(), r2.getText());
    }

    @Test
    public void whenUsedAsSendBody_onlyTextIsRequired() {
        MessageResponse body = new MessageResponse();
        body.setText("Mensaje de prueba");
        assertNotNull(body.getText());
        assertNull(body.getId());
        assertNull(body.getSentAt());
        assertNull(body.getChatId());
        assertNull(body.getSenderId());
    }
}
