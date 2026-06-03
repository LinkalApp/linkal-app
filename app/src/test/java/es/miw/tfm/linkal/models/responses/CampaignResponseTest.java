package es.miw.tfm.linkal.models.responses;

import static org.junit.Assert.*;

import org.junit.Test;

public class CampaignResponseTest {
    @Test
    public void defaultConstructor_allFieldsAreNull() {
        CampaignResponse r = new CampaignResponse();
        assertNull(r.getId());
        assertNull(r.getTitle());
        assertNull(r.getDescription());
        assertNull(r.getRequirements());
        assertNull(r.getReward());
        assertNull(r.getObjective());
        assertNull(r.getStatus());
        assertNull(r.getCreationDate());
        assertNull(r.getBusinessId());
        assertNull(r.getBusinessName());
        assertNull(r.getBusinessCategory());
        assertNull(r.getBusinessDescription());
        assertNull(r.getBusinessWebsite());
        assertNull(r.getBusinessProvince());
        assertNull(r.getBusinessAddress());
        assertNull(r.getBusinessVerified());
    }

    @Test
    public void getId_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setId("f24fc375-c453-458b-bb22-839f551e1e10");
        assertEquals("f24fc375-c453-458b-bb22-839f551e1e10", r.getId());
    }

    @Test
    public void getTitle_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setTitle("Campaña Verano");
        assertEquals("Campaña Verano", r.getTitle());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setDescription("Aumentar visibilidad en redes sociales");
        assertEquals("Aumentar visibilidad en redes sociales", r.getDescription());
    }

    @Test
    public void getRequirements_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setRequirements("Mínimo 500 seguidores en Instagram");
        assertEquals("Mínimo 500 seguidores en Instagram", r.getRequirements());
    }

    @Test
    public void getReward_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setReward("Descuento del 20% en tienda");
        assertEquals("Descuento del 20% en tienda", r.getReward());
    }

    @Test
    public void getReward_withNonMonetaryValue_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setReward("200 euros para gastar en tienda");
        assertEquals("200 euros para gastar en tienda", r.getReward());
    }

    @Test
    public void getObjective_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setObjective("Conseguir 10 publicaciones en Instagram");
        assertEquals("Conseguir 10 publicaciones en Instagram", r.getObjective());
    }

    @Test
    public void getStatus_returnsOpen() {
        CampaignResponse r = new CampaignResponse();
        r.setStatus("OPEN");
        assertEquals("OPEN", r.getStatus());
    }

    @Test
    public void getStatus_returnsClosed() {
        CampaignResponse r = new CampaignResponse();
        r.setStatus("CLOSED");
        assertEquals("CLOSED", r.getStatus());
    }

    @Test
    public void getCreationDate_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setCreationDate("2026-05-30");
        assertEquals("2026-05-30", r.getCreationDate());
    }

    @Test
    public void getBusinessId_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessId("4a7c81f2-2be8-4bac-97c7-33f2ea3ef5f7");
        assertEquals("4a7c81f2-2be8-4bac-97c7-33f2ea3ef5f7", r.getBusinessId());
    }

    @Test
    public void twoInstances_areIndependent() {
        CampaignResponse r1 = new CampaignResponse();
        CampaignResponse r2 = new CampaignResponse();
        r1.setTitle("Campaña A");
        r2.setTitle("Campaña B");
        assertNotEquals(r1.getTitle(), r2.getTitle());
        assertNotSame(r1, r2);
    }

    @Test
    public void setTitle_doesNotAffectOtherFields() {
        CampaignResponse r = new CampaignResponse();
        r.setTitle("Solo título");
        assertNull(r.getDescription());
        assertNull(r.getReward());
        assertNull(r.getStatus());
    }

    // Campos de negocio (open campaigns) -------------------------------------

    @Test
    public void getBusinessName_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessName("Mi Tienda");
        assertEquals("Mi Tienda", r.getBusinessName());
    }

    @Test
    public void getBusinessCategory_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessCategory("Moda y Ropa");
        assertEquals("Moda y Ropa", r.getBusinessCategory());
    }

    @Test
    public void getBusinessDescription_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessDescription("Tienda de ropa sostenible");
        assertEquals("Tienda de ropa sostenible", r.getBusinessDescription());
    }

    @Test
    public void getBusinessWebsite_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessWebsite("https://mitienda.es");
        assertEquals("https://mitienda.es", r.getBusinessWebsite());
    }

    @Test
    public void getBusinessProvince_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessProvince("Madrid");
        assertEquals("Madrid", r.getBusinessProvince());
    }

    @Test
    public void getBusinessAddress_returnsCorrectValue() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessAddress("Calle Gran Vía 1");
        assertEquals("Calle Gran Vía 1", r.getBusinessAddress());
    }

    @Test
    public void getBusinessVerified_returnsTrueWhenVerified() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessVerified(true);
        assertTrue(r.getBusinessVerified());
    }

    @Test
    public void getBusinessVerified_returnsFalseWhenNotVerified() {
        CampaignResponse r = new CampaignResponse();
        r.setBusinessVerified(false);
        assertFalse(r.getBusinessVerified());
    }

    @Test
    public void businessFields_areIndependentFromCampaignFields() {
        CampaignResponse r = new CampaignResponse();
        r.setTitle("Campaña Verano");
        r.setBusinessName("Mi Tienda");
        r.setBusinessCategory("Moda");
        assertEquals("Campaña Verano", r.getTitle());
        assertEquals("Mi Tienda", r.getBusinessName());
        assertEquals("Moda", r.getBusinessCategory());
        assertNull(r.getDescription());
        assertNull(r.getBusinessWebsite());
    }
}
