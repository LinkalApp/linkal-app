package es.miw.tfm.linkal.models.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ChatResponseTest {
    @Test
    public void defaultConstructor_allFieldsAreNull() {
        ChatResponse r = new ChatResponse();
        assertNull(r.getId());
        assertNull(r.getMatchId());
        assertNull(r.getCampaignId());
        assertNull(r.getDisplayName());
        assertNull(r.getCampaignTitle());
        assertNull(r.getLastMessage());
        assertNull(r.getLastMessageAt());
    }

    @Test
    public void setId_andGetId_roundTrip() {
        ChatResponse r = new ChatResponse();
        r.setId("abc-123");
        assertEquals("abc-123", r.getId());
    }

    @Test
    public void setDisplayName_andGetDisplayName_roundTrip() {
        ChatResponse r = new ChatResponse();
        r.setDisplayName("Nike Spain");
        assertEquals("Nike Spain", r.getDisplayName());
    }


    @Test
    public void setCampaignTitle_andGetCampaignTitle_roundTrip() {
        ChatResponse r = new ChatResponse();
        r.setCampaignTitle("Campaña Verano 2025");
        assertEquals("Campaña Verano 2025", r.getCampaignTitle());
    }

    @Test
    public void setLastMessage_andGetLastMessage_roundTrip() {
        ChatResponse r = new ChatResponse();
        r.setLastMessage("Hola, ¿seguimos adelante?");
        assertEquals("Hola, ¿seguimos adelante?", r.getLastMessage());
    }

    @Test
    public void setLastMessageAt_andGetLastMessageAt_roundTrip() {
        ChatResponse r = new ChatResponse();
        r.setLastMessageAt("2025-06-16T14:30:00");
        assertEquals("2025-06-16T14:30:00", r.getLastMessageAt());
    }

    @Test
    public void setMatchId_andGetMatchId_roundTrip() {
        ChatResponse r = new ChatResponse();
        r.setMatchId("match-uuid");
        assertEquals("match-uuid", r.getMatchId());
    }

    @Test
    public void setCampaignId_andGetCampaignId_roundTrip() {
        ChatResponse r = new ChatResponse();
        r.setCampaignId("campaign-uuid");
        assertEquals("campaign-uuid", r.getCampaignId());
    }

    @Test
    public void twoChatResponses_withSameDisplayNameButDifferentCampaign_areDifferent() {
        ChatResponse r1 = new ChatResponse();
        r1.setDisplayName("Nike Spain");
        r1.setCampaignTitle("Campaña Verano");

        ChatResponse r2 = new ChatResponse();
        r2.setDisplayName("Nike Spain");
        r2.setCampaignTitle("Colección Otoño");

        assertEquals(r1.getDisplayName(), r2.getDisplayName());
        assertNotEquals(r1.getCampaignTitle(), r2.getCampaignTitle());
    }

    @Test
    public void setCampaignTitle_toNull_returnsNull() {
        ChatResponse r = new ChatResponse();
        r.setCampaignTitle("Algo");
        r.setCampaignTitle(null);
        assertNull(r.getCampaignTitle());
    }
}
