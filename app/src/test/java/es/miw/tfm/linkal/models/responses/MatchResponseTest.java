package es.miw.tfm.linkal.models.responses;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class MatchResponseTest {

    @Test
    public void defaultConstructor_baseFieldsAreNull() {
        MatchResponse r = new MatchResponse();
        assertNull(r.getId());
        assertNull(r.getStatus());
        assertNull(r.getCreatedAt());
        assertNull(r.getMatchedAt());
        assertNull(r.getCampaignId());
        assertNull(r.getInfluencerId());
    }

    @Test
    public void defaultConstructor_campaignFieldsAreNull() {
        MatchResponse r = new MatchResponse();
        assertNull(r.getCampaignTitle());
        assertNull(r.getCampaignDescription());
        assertNull(r.getCampaignObjective());
        assertNull(r.getCampaignRequirements());
        assertNull(r.getCampaignReward());
        assertNull(r.getCampaignStatus());
        assertNull(r.getCampaignCreationDate());
    }

    @Test
    public void defaultConstructor_businessFieldsAreNull() {
        MatchResponse r = new MatchResponse();
        assertNull(r.getBusinessName());
        assertNull(r.getBusinessCategory());
        assertNull(r.getBusinessDescription());
        assertNull(r.getBusinessWebsite());
        assertNull(r.getBusinessProvince());
        assertNull(r.getBusinessAddress());
        assertNull(r.getBusinessVerified());
    }

    @Test
    public void defaultConstructor_influencerFieldsAreNull() {
        MatchResponse r = new MatchResponse();
        assertNull(r.getInfluencerName());
        assertNull(r.getInfluencerArtisticName());
        assertNull(r.getInfluencerDescription());
        assertNull(r.getInfluencerEmail());
        assertNull(r.getInfluencerInstagram());
        assertNull(r.getInfluencerTiktok());
        assertNull(r.getInfluencerYoutube());
        assertNull(r.getInfluencerVerified());
        assertNull(r.getInfluencerInterests());
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
    public void getCampaignTitle_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignTitle("Campaña Verano 2026");
        assertEquals("Campaña Verano 2026", r.getCampaignTitle());
    }

    @Test
    public void getCampaignDescription_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignDescription("Descripción de campaña");
        assertEquals("Descripción de campaña", r.getCampaignDescription());
    }

    @Test
    public void getCampaignObjective_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignObjective("Aumentar visibilidad");
        assertEquals("Aumentar visibilidad", r.getCampaignObjective());
    }

    @Test
    public void getCampaignRequirements_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignRequirements("Mínimo 10k seguidores");
        assertEquals("Mínimo 10k seguidores", r.getCampaignRequirements());
    }

    @Test
    public void getCampaignReward_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignReward("500€ + producto");
        assertEquals("500€ + producto", r.getCampaignReward());
    }

    @Test
    public void getCampaignStatus_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignStatus("OPEN");
        assertEquals("OPEN", r.getCampaignStatus());
    }

    @Test
    public void getCampaignCreationDate_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setCampaignCreationDate("2026-01-15");
        assertEquals("2026-01-15", r.getCampaignCreationDate());
    }

    @Test
    public void getBusinessName_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setBusinessName("Nike Spain");
        assertEquals("Nike Spain", r.getBusinessName());
    }

    @Test
    public void getBusinessCategory_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setBusinessCategory("Moda");
        assertEquals("Moda", r.getBusinessCategory());
    }

    @Test
    public void getBusinessDescription_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setBusinessDescription("Empresa de calzado deportivo");
        assertEquals("Empresa de calzado deportivo", r.getBusinessDescription());
    }

    @Test
    public void getBusinessWebsite_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setBusinessWebsite("https://nike.com");
        assertEquals("https://nike.com", r.getBusinessWebsite());
    }

    @Test
    public void getBusinessProvince_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setBusinessProvince("Madrid");
        assertEquals("Madrid", r.getBusinessProvince());
    }

    @Test
    public void getBusinessAddress_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setBusinessAddress("Calle Gran Vía 1");
        assertEquals("Calle Gran Vía 1", r.getBusinessAddress());
    }

    @Test
    public void getBusinessVerified_returnsTrue() {
        MatchResponse r = new MatchResponse();
        r.setBusinessVerified(true);
        assertTrue(r.getBusinessVerified());
    }

    @Test
    public void getBusinessVerified_returnsFalse() {
        MatchResponse r = new MatchResponse();
        r.setBusinessVerified(false);
        assertFalse(r.getBusinessVerified());
    }

    @Test
    public void getInfluencerName_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerName("Ana López");
        assertEquals("Ana López", r.getInfluencerName());
    }

    @Test
    public void getInfluencerArtisticName_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerArtisticName("AnaModa");
        assertEquals("AnaModa", r.getInfluencerArtisticName());
    }

    @Test
    public void getInfluencerDescription_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerDescription("Creadora de contenido de moda");
        assertEquals("Creadora de contenido de moda", r.getInfluencerDescription());
    }

    @Test
    public void getInfluencerEmail_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerEmail("ana@linkal.es");
        assertEquals("ana@linkal.es", r.getInfluencerEmail());
    }

    @Test
    public void getInfluencerInstagram_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerInstagram("@anamoda");
        assertEquals("@anamoda", r.getInfluencerInstagram());
    }

    @Test
    public void getInfluencerTiktok_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerTiktok("@anamodatiktok");
        assertEquals("@anamodatiktok", r.getInfluencerTiktok());
    }

    @Test
    public void getInfluencerYoutube_returnsCorrectValue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerYoutube("youtube.com/anamoda");
        assertEquals("youtube.com/anamoda", r.getInfluencerYoutube());
    }

    @Test
    public void getInfluencerVerified_returnsTrue() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerVerified(true);
        assertTrue(r.getInfluencerVerified());
    }

    @Test
    public void getInfluencerVerified_returnsFalse() {
        MatchResponse r = new MatchResponse();
        r.setInfluencerVerified(false);
        assertFalse(r.getInfluencerVerified());
    }

    @Test
    public void getInfluencerInterests_returnsCorrectList() {
        MatchResponse r = new MatchResponse();
        List<String> interests = Arrays.asList("Moda", "Viajes", "Fitness");
        r.setInfluencerInterests(interests);
        assertEquals(3, r.getInfluencerInterests().size());
        assertEquals("Moda", r.getInfluencerInterests().get(0));
    }

    @Test
    public void twoInstances_areIndependent() {
        MatchResponse r1 = new MatchResponse();
        MatchResponse r2 = new MatchResponse();
        r1.setInfluencerName("Ana");
        r2.setInfluencerName("Luis");
        assertNotEquals(r1.getInfluencerName(), r2.getInfluencerName());
        assertNotSame(r1, r2);
    }

    @Test
    public void setCampaignTitle_doesNotAffectOtherFields() {
        MatchResponse r = new MatchResponse();
        r.setCampaignTitle("Campaña Test");
        assertNull(r.getInfluencerName());
        assertNull(r.getBusinessName());
        assertNull(r.getStatus());
    }
}