package es.miw.tfm.linkal.models.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UpdateBusinessRequestTest {

    @Test
    public void constructor_setsAllFields() {
        UpdateBusinessRequest request = new UpdateBusinessRequest(
                "Mi Empresa", "611000000", "Descripción del negocio",
                "Calle Mayor 1", "Madrid", "https://miempresa.com"
        );

        assertEquals("Mi Empresa",              request.getName());
        assertEquals("611000000",               request.getPhoneNumber());
        assertEquals("Descripción del negocio", request.getDescription());
        assertEquals("Calle Mayor 1",           request.getAddress());
        assertEquals("Madrid",                  request.getProvince());
        assertEquals("https://miempresa.com",   request.getWebsite());
    }

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        UpdateBusinessRequest request = new UpdateBusinessRequest();

        assertNull(request.getName());
        assertNull(request.getPhoneNumber());
        assertNull(request.getDescription());
        assertNull(request.getAddress());
        assertNull(request.getProvince());
        assertNull(request.getWebsite());
    }

    @Test
    public void setName_updatesName() {
        UpdateBusinessRequest request = new UpdateBusinessRequest();
        request.setName("Nuevo Nombre");
        assertEquals("Nuevo Nombre", request.getName());
    }

    @Test
    public void setPhoneNumber_updatesPhoneNumber() {
        UpdateBusinessRequest request = new UpdateBusinessRequest();
        request.setPhoneNumber("699000111");
        assertEquals("699000111", request.getPhoneNumber());
    }

    @Test
    public void setDescription_updatesDescription() {
        UpdateBusinessRequest request = new UpdateBusinessRequest();
        request.setDescription("Nueva descripción");
        assertEquals("Nueva descripción", request.getDescription());
    }

    @Test
    public void setAddress_updatesAddress() {
        UpdateBusinessRequest request = new UpdateBusinessRequest();
        request.setAddress("Avenida Nueva 42");
        assertEquals("Avenida Nueva 42", request.getAddress());
    }

    @Test
    public void setProvince_updatesProvince() {
        UpdateBusinessRequest request = new UpdateBusinessRequest();
        request.setProvince("Barcelona");
        assertEquals("Barcelona", request.getProvince());
    }

    @Test
    public void setWebsite_updatesWebsite() {
        UpdateBusinessRequest request = new UpdateBusinessRequest();
        request.setWebsite("https://nuevaweb.com");
        assertEquals("https://nuevaweb.com", request.getWebsite());
    }
}
