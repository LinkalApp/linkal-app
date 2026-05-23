package es.miw.tfm.linkal.models.responses;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InfluencerProfileResponseTest {

    @Test
    public void getName_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setName("Laura");
        assertEquals("Laura", r.getName());
    }

    @Test
    public void getEmail_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setEmail("laura@test.com");
        assertEquals("laura@test.com", r.getEmail());
    }

    @Test
    public void getPhoneNumber_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setPhoneNumber("666000000");
        assertEquals("666000000", r.getPhoneNumber());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setDescription("Bio de prueba");
        assertEquals("Bio de prueba", r.getDescription());
    }

    @Test
    public void getArtisticName_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setArtisticName("LauraStyle");
        assertEquals("LauraStyle", r.getArtisticName());
    }

    @Test
    public void getInterests_returnsCorrectList() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        List<String> interests = Arrays.asList("Moda", "Viajes");
        r.setInterests(interests);
        assertEquals(interests, r.getInterests());
    }

    @Test
    public void getInstagram_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setInstagram("@laurastyle");
        assertEquals("@laurastyle", r.getInstagram());
    }

    @Test
    public void getTiktok_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setTiktok("@lauratiktok");
        assertEquals("@lauratiktok", r.getTiktok());
    }

    @Test
    public void getYoutube_returnsCorrectValue() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        r.setYoutube("LauraYT");
        assertEquals("LauraYT", r.getYoutube());
    }

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        InfluencerProfileResponse r = new InfluencerProfileResponse();
        assertNull(r.getName());
        assertNull(r.getEmail());
        assertNull(r.getPhoneNumber());
        assertNull(r.getDescription());
        assertNull(r.getArtisticName());
        assertNull(r.getInterests());
        assertNull(r.getInstagram());
        assertNull(r.getTiktok());
        assertNull(r.getYoutube());
    }
}