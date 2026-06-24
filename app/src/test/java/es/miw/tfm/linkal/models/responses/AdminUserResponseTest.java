package es.miw.tfm.linkal.models.responses;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AdminUserResponseTest {

    @Test
    public void defaultConstructor_baseFieldsAreNull() {
        AdminUserResponse r = new AdminUserResponse();
        assertNull(r.getId());
        assertNull(r.getName());
        assertNull(r.getEmail());
        assertNull(r.getPhoneNumber());
        assertNull(r.getDescription());
        assertNull(r.getVerified());
        assertNull(r.getRole());
    }

    @Test
    public void defaultConstructor_influencerFieldsAreNull() {
        AdminUserResponse r = new AdminUserResponse();
        assertNull(r.getArtisticName());
        assertNull(r.getInterests());
        assertNull(r.getInstagram());
        assertNull(r.getTiktok());
        assertNull(r.getYoutube());
    }

    @Test
    public void defaultConstructor_businessFieldsAreNull() {
        AdminUserResponse r = new AdminUserResponse();
        assertNull(r.getAddress());
        assertNull(r.getProvince());
        assertNull(r.getWebsite());
        assertNull(r.getCategory());
    }

    @Test
    public void getId_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setId("uuid-123");
        assertEquals("uuid-123", r.getId());
    }

    @Test
    public void getName_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setName("Ana García");
        assertEquals("Ana García", r.getName());
    }

    @Test
    public void getEmail_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setEmail("ana@linkal.es");
        assertEquals("ana@linkal.es", r.getEmail());
    }

    @Test
    public void getRole_influencer_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setRole("INFLUENCER");
        assertEquals("INFLUENCER", r.getRole());
    }

    @Test
    public void getRole_business_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setRole("BUSINESS");
        assertEquals("BUSINESS", r.getRole());
    }

    @Test
    public void getVerified_true_returnsTrue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setVerified(true);
        assertTrue(r.getVerified());
    }

    @Test
    public void getVerified_false_returnsFalse() {
        AdminUserResponse r = new AdminUserResponse();
        r.setVerified(false);
        assertFalse(r.getVerified());
    }

    @Test
    public void getPhoneNumber_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setPhoneNumber("600123456");
        assertEquals("600123456", r.getPhoneNumber());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setDescription("Una descripción de prueba");
        assertEquals("Una descripción de prueba", r.getDescription());
    }

    @Test
    public void getArtisticName_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setArtisticName("AnaFashion");
        assertEquals("AnaFashion", r.getArtisticName());
    }

    @Test
    public void getInterests_returnsCorrectList() {
        AdminUserResponse r = new AdminUserResponse();
        List<String> interests = Arrays.asList("Moda", "Viajes", "Fitness");
        r.setInterests(interests);
        assertEquals(3, r.getInterests().size());
        assertEquals("Moda", r.getInterests().get(0));
    }

    @Test
    public void getInstagram_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setInstagram("@anafashion");
        assertEquals("@anafashion", r.getInstagram());
    }

    @Test
    public void getTiktok_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setTiktok("@anafashiontk");
        assertEquals("@anafashiontk", r.getTiktok());
    }

    @Test
    public void getYoutube_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setYoutube("youtube.com/anafashion");
        assertEquals("youtube.com/anafashion", r.getYoutube());
    }

    @Test
    public void getCategory_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setCategory("Moda y Ropa");
        assertEquals("Moda y Ropa", r.getCategory());
    }

    @Test
    public void getProvince_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setProvince("Barcelona");
        assertEquals("Barcelona", r.getProvince());
    }

    @Test
    public void getAddress_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setAddress("Paseo de Gracia 100");
        assertEquals("Paseo de Gracia 100", r.getAddress());
    }

    @Test
    public void getWebsite_returnsCorrectValue() {
        AdminUserResponse r = new AdminUserResponse();
        r.setWebsite("https://marca.com");
        assertEquals("https://marca.com", r.getWebsite());
    }

    @Test
    public void twoInstances_areIndependent() {
        AdminUserResponse r1 = new AdminUserResponse();
        AdminUserResponse r2 = new AdminUserResponse();
        r1.setName("Ana");
        r2.setName("Luis");
        assertNotEquals(r1.getName(), r2.getName());
        assertNotSame(r1, r2);
    }

    @Test
    public void setRole_doesNotAffectOtherFields() {
        AdminUserResponse r = new AdminUserResponse();
        r.setRole("INFLUENCER");
        assertNull(r.getName());
        assertNull(r.getEmail());
        assertNull(r.getArtisticName());
    }
}

