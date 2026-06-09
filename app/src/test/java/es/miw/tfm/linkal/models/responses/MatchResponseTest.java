package es.miw.tfm.linkal.models.responses;

import org.junit.Test;

import static org.junit.Assert.*;

public class MatchResponseTest {

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        MatchResponse r = new MatchResponse();
        assertNull(r.getId());
        assertNull(r.getStatus());
        assertNull(r.getCreatedAt());
        assertNull(r.getMatchedAt());
        assertNull(r.getCampaignId());
        assertNull(r.getInfluencerId());
    }

    @Test
    public void getId_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setId("abc-123");
        assertEquals("abc-123", r.getId());
    }

    @Test
    public void getStatus_returnsPending() {
        MatchResponse r = new MatchResponse();
        r.setStatus("PENDING");
        assertEquals("PENDING", r.getStatus());
    }

    @Test
    public void getStatus_returnsCompleted() {
        MatchResponse r = new MatchResponse();
        r.setStatus("COMPLETED");
        assertEquals("COMPLETED", r.getStatus());
    }

    @Test
    public void getCreatedAt_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCreatedAt("2026-06-01T10:00:00");
        assertEquals("2026-06-01T10:00:00", r.getCreatedAt());
    }

    @Test
    public void getMatchedAt_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setMatchedAt("2026-06-02T12:30:00");
        assertEquals("2026-06-02T12:30:00", r.getMatchedAt());
    }

    @Test
    public void getMatchedAt_isNullByDefault() {
        MatchResponse r = new MatchResponse();
        assertNull(r.getMatchedAt());
    }

    @Test
    public void getCampaignId_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignId("campaign-uuid-001");
        assertEquals("campaign-uuid-001", r.getCampaignId());
    }

    @Test
    public void getInfluencerId_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerId("influencer-uuid-001");
        assertEquals("influencer-uuid-001", r.getInfluencerId());
    }

    @Test
    public void getInfluencerId_isNullByDefault() {
        MatchResponse r = new MatchResponse();
        assertNull(r.getInfluencerId());
    }


    @Test
    public void twoInstances_areIndependent() {
        MatchResponse r1 = new MatchResponse();
        MatchResponse r2 = new MatchResponse();
        r1.setStatus("PENDING");
        r2.setStatus("COMPLETED");
        assertNotEquals(r1.getStatus(), r2.getStatus());
        assertNotSame(r1, r2);
    }

    @Test
    public void setStatus_doesNotAffectOtherFields() {
        MatchResponse r = new MatchResponse();
        r.setStatus("PENDING");
        assertNull(r.getCampaignId());
        assertNull(r.getMatchedAt());
    }
}