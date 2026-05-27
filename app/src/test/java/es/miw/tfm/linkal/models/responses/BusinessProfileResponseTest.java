package es.miw.tfm.linkal.models.responses;

import org.junit.Test;

import static org.junit.Assert.*;

public class BusinessProfileResponseTest {
    @Test
    public void getName_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setName("El Pescador");
        assertEquals("El Pescador", r.getName());
    }

    @Test
    public void getEmail_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setEmail("empresa@test.com");
        assertEquals("empresa@test.com", r.getEmail());
    }

    @Test
    public void getPhoneNumber_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setPhoneNumber("611000000");
        assertEquals("611000000", r.getPhoneNumber());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setDescription("Restaurante / bar a pie de playa");
        assertEquals("Restaurante / bar a pie de playa", r.getDescription());
    }

    @Test
    public void getDirection_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setAddress("Calle Mayor 1");
        assertEquals("Calle Mayor 1", r.getAddress());
    }

    @Test
    public void getProvince_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setProvince("Málaga");
        assertEquals("Málaga", r.getProvince());
    }

    @Test
    public void getWebPage_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setWebsite("https://miempresa.com");
        assertEquals("https://miempresa.com", r.getWebsite());
    }

    @Test
    public void getCategory_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setCategory("Restauración y Hostelería");
        assertEquals("Restauración y Hostelería", r.getCategory());
    }

    @Test
    public void getVerified_returnsTrue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setVerified(true);
        assertTrue(r.getVerified());
    }

    @Test
    public void getVerified_returnsFalse() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setVerified(false);
        assertFalse(r.getVerified());
    }

    @Test
    public void getAverageRating_returnsCorrectValue() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setAverageRating(3.8);
        assertEquals(3.8, r.getAverageRating(), 0.001);
    }

    @Test
    public void getAverageRating_returnsNullByDefault() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        assertNull(r.getAverageRating());
    }

    @Test
    public void getAverageRating_returnsPerfectScore() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setAverageRating(5.0);
        assertEquals(5.0, r.getAverageRating(), 0.001);
    }

    @Test
    public void getAverageRating_returnsMinimumScore() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setAverageRating(1.0);
        assertEquals(1.0, r.getAverageRating(), 0.001);
    }

    @Test
    public void getAverageRating_returnsDecimalScore() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        r.setAverageRating(2.5);
        assertEquals(2.5, r.getAverageRating(), 0.001);
    }

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        BusinessProfileResponse r = new BusinessProfileResponse();
        assertNull(r.getName());
        assertNull(r.getEmail());
        assertNull(r.getPhoneNumber());
        assertNull(r.getDescription());
        assertNull(r.getAddress());
        assertNull(r.getProvince());
        assertNull(r.getWebsite());
        assertNull(r.getCategory());
        assertNull(r.getVerified());
        assertNull(r.getAverageRating());
    }
}
