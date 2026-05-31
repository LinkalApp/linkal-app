package es.miw.tfm.linkal.models.requests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateCampaignRequestTest {
    private UpdateCampaignRequest request;

    @Before
    public void setUp() {
        request = new UpdateCampaignRequest(
                "Título actualizado",
                "Descripción actualizada",
                "Nuevos requisitos",
                "Nueva recompensa",
                "Nuevo objetivo",
                "IN_PROGRESS"
        );
    }

    @Test
    public void getTitle_returnsCorrectValue() {
        assertEquals("Título actualizado", request.getTitle());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        assertEquals("Descripción actualizada", request.getDescription());
    }

    @Test
    public void getRequirements_returnsCorrectValue() {
        assertEquals("Nuevos requisitos", request.getRequirements());
    }

    @Test
    public void getReward_returnsCorrectValue() {
        assertEquals("Nueva recompensa", request.getReward());
    }

    @Test
    public void getObjective_returnsCorrectValue() {
        assertEquals("Nuevo objetivo", request.getObjective());
    }

    @Test
    public void getStatus_returnsCorrectValue() {
        assertEquals("IN_PROGRESS", request.getStatus());
    }

    @Test
    public void setTitle_updatesValue() {
        request.setTitle("Otro título");
        assertEquals("Otro título", request.getTitle());
    }

    @Test
    public void setReward_updatesValue() {
        request.setReward("Otra recompensa");
        assertEquals("Otra recompensa", request.getReward());
    }

    @Test
    public void setRequirements_updatesValue() {
        request.setRequirements("Otros requisitos");
        assertEquals("Otros requisitos", request.getRequirements());
    }

    @Test
    public void setObjective_updatesValue() {
        request.setObjective("Otro objetivo");
        assertEquals("Otro objetivo", request.getObjective());
    }

    @Test
    public void setDescription_updatesValue() {
        request.setDescription("Otra descripcion");
        assertEquals("Otra descripcion", request.getDescription());
    }

    @Test
    public void setStatus_toClosed_updatesValue() {
        request.setStatus("CLOSED");
        assertEquals("CLOSED", request.getStatus());
    }

    @Test
    public void setStatus_toOpen_updatesValue() {
        request.setStatus("OPEN");
        assertEquals("OPEN", request.getStatus());
    }

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        UpdateCampaignRequest empty = new UpdateCampaignRequest();
        assertNull(empty.getTitle());
        assertNull(empty.getDescription());
        assertNull(empty.getRequirements());
        assertNull(empty.getReward());
        assertNull(empty.getObjective());
        assertNull(empty.getStatus());
    }

    @Test
    public void constructor_withNullStatus_storesNull() {
        UpdateCampaignRequest r = new UpdateCampaignRequest(
                "Título", "Desc", "Req", "Rew", "Obj", null
        );
        assertNull(r.getStatus());
    }

    @Test
    public void constructor_preservesAllFields() {
        assertEquals("Título actualizado", request.getTitle());
        assertEquals("Descripción actualizada", request.getDescription());
        assertEquals("Nuevos requisitos", request.getRequirements());
        assertEquals("Nueva recompensa", request.getReward());
        assertEquals("Nuevo objetivo", request.getObjective());
        assertEquals("IN_PROGRESS", request.getStatus());
    }
}
