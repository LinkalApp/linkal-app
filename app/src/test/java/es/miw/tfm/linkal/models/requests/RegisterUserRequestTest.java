package es.miw.tfm.linkal.models.requests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterUserRequestTest {

    private RegisterUserRequest request;

    @Before
    public void setUp() {
        request = new RegisterUserRequest(
                "Ana García",
                "ana@example.com",
                "password123",
                "600123456",
                "Descripción de prueba",
                "BUSINESS"
        );
    }

    @Test
    public void getName_returnsCorrectValue() {
        assertEquals("Ana García", request.getName());
    }

    @Test
    public void getEmail_returnsCorrectValue() {
        assertEquals("ana@example.com", request.getEmail());
    }

    @Test
    public void getPassword_returnsCorrectValue() {
        assertEquals("password123", request.getPassword());
    }

    @Test
    public void getPhoneNumber_returnsCorrectValue() {
        assertEquals("600123456", request.getPhoneNumber());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        assertEquals("Descripción de prueba", request.getDescription());
    }

    @Test
    public void getRole_returnsCorrectValue() {
        assertEquals("BUSINESS", request.getRole());
    }

    @Test
    public void constructor_withNullValues_doesNotThrow() {
        RegisterUserRequest nullRequest = new RegisterUserRequest(null, null, null, null, null, null);
        assertNull(nullRequest.getName());
        assertNull(nullRequest.getEmail());
        assertNull(nullRequest.getPassword());
        assertNull(nullRequest.getPhoneNumber());
        assertNull(nullRequest.getDescription());
        assertNull(nullRequest.getRole());
    }

    @Test
    public void constructor_withEmptyStrings_storesEmptyStrings() {
        RegisterUserRequest emptyRequest = new RegisterUserRequest("", "", "", "", "", "");
        assertEquals("", emptyRequest.getName());
        assertEquals("", emptyRequest.getEmail());
        assertEquals("", emptyRequest.getPassword());
        assertEquals("", emptyRequest.getPhoneNumber());
        assertEquals("", emptyRequest.getDescription());
        assertEquals("", emptyRequest.getRole());
    }
}

