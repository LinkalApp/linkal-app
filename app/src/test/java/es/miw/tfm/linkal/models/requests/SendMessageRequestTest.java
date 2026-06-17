package es.miw.tfm.linkal.models.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SendMessageRequestTest {
    @Test
    public void constructor_setsText() {
        SendMessageRequest r = new SendMessageRequest("Hola!");
        assertEquals("Hola!", r.getText());
    }

    @Test
    public void constructor_withEmptyText_setsEmptyString() {
        SendMessageRequest r = new SendMessageRequest("");
        assertEquals("", r.getText());
    }

    @Test
    public void constructor_withNull_setsNull() {
        SendMessageRequest r = new SendMessageRequest(null);
        assertNull(r.getText());
    }

    @Test
    public void setText_andGetText_roundTrip() {
        SendMessageRequest r = new SendMessageRequest("inicial");
        r.setText("nuevo texto");
        assertEquals("nuevo texto", r.getText());
    }

    @Test
    public void setText_toNull_returnsNull() {
        SendMessageRequest r = new SendMessageRequest("Hola");
        r.setText(null);
        assertNull(r.getText());
    }

    @Test
    public void setText_withLongMessage_preservesFullText() {
        String longText = "a".repeat(2000);
        SendMessageRequest r = new SendMessageRequest(longText);
        assertEquals(2000, r.getText().length());
    }

    @Test
    public void twoInstances_areIndependent() {
        SendMessageRequest r1 = new SendMessageRequest("Hola");
        SendMessageRequest r2 = new SendMessageRequest("Adios");
        assertEquals("Hola", r1.getText());
        assertEquals("Adios", r2.getText());
    }

    @Test
    public void modifyingOneInstance_doesNotAffectAnother() {
        SendMessageRequest r1 = new SendMessageRequest("original");
        SendMessageRequest r2 = new SendMessageRequest("original");
        r1.setText("modificado");
        assertEquals("modificado", r1.getText());
        assertEquals("original", r2.getText());
    }
}
