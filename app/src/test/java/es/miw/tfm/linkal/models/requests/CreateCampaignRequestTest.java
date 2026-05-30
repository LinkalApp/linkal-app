package es.miw.tfm.linkal.models.requests;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreateCampaignRequestTest {
    @Test
    public void constructor_setsTitle() {
        CreateCampaignRequest request = buildRequest();
        assertEquals("Campaña Verano", request.getTitle());
    }

    @Test
    public void constructor_setsDescription() {
        CreateCampaignRequest request = buildRequest();
        assertEquals("Descripción de la campaña", request.getDescription());
    }

    @Test
    public void constructor_setsRequirements() {
        CreateCampaignRequest request = buildRequest();
        assertEquals("500 seguidores mínimo", request.getRequirements());
    }

    @Test
    public void constructor_setsReward() {
        CreateCampaignRequest request = buildRequest();
        assertEquals("Descuento 20%", request.getReward());
    }

    @Test
    public void constructor_setsObjective() {
        CreateCampaignRequest request = buildRequest();
        assertEquals("Aumentar ventas", request.getObjective());
    }

    @Test
    public void constructor_withDifferentValues_setsCorrectly() {
        CreateCampaignRequest request = new CreateCampaignRequest(
                "Campaña Navidad",
                "Campaña especial de navidad",
                "1000 seguidores en cualquier red",
                "Producto gratis valorado en 50€",
                "Aumentar ventas en temporada alta"
        );

        assertEquals("Campaña Navidad", request.getTitle());
        assertEquals("Campaña especial de navidad", request.getDescription());
        assertEquals("1000 seguidores en cualquier red", request.getRequirements());
        assertEquals("Producto gratis valorado en 50€", request.getReward());
        assertEquals("Aumentar ventas en temporada alta", request.getObjective());
    }

    @Test
    public void constructor_withNullValues_fieldsAreNull() {
        CreateCampaignRequest request = new CreateCampaignRequest(null, null, null, null, null);

        assertNull(request.getTitle());
        assertNull(request.getDescription());
        assertNull(request.getRequirements());
        assertNull(request.getReward());
        assertNull(request.getObjective());
    }

    // helpers ---------------------------------------------------------------------

    private CreateCampaignRequest buildRequest() {
        return new CreateCampaignRequest(
                "Campaña Verano",
                "Descripción de la campaña",
                "500 seguidores mínimo",
                "Descuento 20%",
                "Aumentar ventas"
        );
    }
}
