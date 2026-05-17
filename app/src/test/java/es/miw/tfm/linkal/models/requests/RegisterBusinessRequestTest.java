package es.miw.tfm.linkal.models.requests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterBusinessRequestTest {

    private RegisterBusinessRequest request;

    @Before
    public void setUp() {
        request = new RegisterBusinessRequest(
                "Empresa SA",
                "empresa@example.com",
                "securePass1",
                "911234567",
                "Una empresa de prueba",
                "Calle Mayor 10",
                "Madrid",
                "https://empresa.com",
                "Tecnología"
        );
    }

    // CAMPOS HEREDADOS DE RegisterUserRequest -------------------------

    @Test
    public void getName_returnsCorrectValue() {
        assertEquals("Empresa SA", request.getName());
    }

    @Test
    public void getEmail_returnsCorrectValue() {
        assertEquals("empresa@example.com", request.getEmail());
    }

    @Test
    public void getPassword_returnsCorrectValue() {
        assertEquals("securePass1", request.getPassword());
    }

    @Test
    public void getPhoneNumber_returnsCorrectValue() {
        assertEquals("911234567", request.getPhoneNumber());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        assertEquals("Una empresa de prueba", request.getDescription());
    }

    @Test
    public void getRole_isAlwaysBusiness() {
        assertEquals("BUSINESS", request.getRole());
    }

    // CAMPOS PROPIOS DE RegisterBusinessRequest ---------------------------

    @Test
    public void getDirection_returnsCorrectValue() {
        assertEquals("Calle Mayor 10", request.getAddress());
    }

    @Test
    public void getProvince_returnsCorrectValue() {
        assertEquals("Madrid", request.getProvince());
    }

    @Test
    public void getWebPage_returnsCorrectValue() {
        assertEquals("https://empresa.com", request.getWebsite());
    }

    @Test
    public void getCategory_returnsCorrectValue() {
        assertEquals("Tecnología", request.getCategory());
    }

    // HERENCIA -----------------------------------------------------------

    @Test
    public void isInstanceOfRegisterUserRequest() {
        assertTrue(request instanceof RegisterUserRequest);
    }

    @Test
    public void constructor_withNullOptionalFields_doesNotThrow() {
        RegisterBusinessRequest nullFields = new RegisterBusinessRequest(
                "Empresa", "e@e.com", "pass", "123", "desc",
                null, null, null, null
        );
        assertNull(nullFields.getAddress());
        assertNull(nullFields.getProvince());
        assertNull(nullFields.getWebsite());
        assertNull(nullFields.getCategory());
        assertEquals("BUSINESS", nullFields.getRole());
    }
}
