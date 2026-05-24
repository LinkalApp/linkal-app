package es.miw.tfm.linkal.models.requests;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UpdateInfluencerRequestTest {

    // Constructor por defecto ---------------------------------------------

    @Test
    public void defaultConstructor_allFieldsAreNull() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();

        assertNull(request.getName());
        assertNull(request.getPhoneNumber());
        assertNull(request.getDescription());
        assertNull(request.getArtisticName());
        assertNull(request.getInterests());
        assertNull(request.getInstagram());
        assertNull(request.getTiktok());
        assertNull(request.getYoutube());
    }

    // Constructor completo -------------------------------------------------------------

    @Test
    public void paramConstructor_setsAllFields() {
        List<String> interests = Arrays.asList("Moda", "Viajes");

        UpdateInfluencerRequest request = new UpdateInfluencerRequest(
                "Laura",
                "666000000",
                "Bio de prueba",
                "LauraStyle",
                interests,
                "@laura_ig",
                "@laura_tt",
                "LauraYT"
        );

        assertEquals("Laura",       request.getName());
        assertEquals("666000000",   request.getPhoneNumber());
        assertEquals("Bio de prueba", request.getDescription());
        assertEquals("LauraStyle",  request.getArtisticName());
        assertEquals(interests,     request.getInterests());
        assertEquals("@laura_ig",   request.getInstagram());
        assertEquals("@laura_tt",   request.getTiktok());
        assertEquals("LauraYT",     request.getYoutube());
    }

    @Test
    public void paramConstructor_withNullSocialFields_doesNotThrow() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest(
                "Laura", null, null, null, null,
                null, null, null
        );

        assertEquals("Laura", request.getName());
        assertNull(request.getInstagram());
        assertNull(request.getTiktok());
        assertNull(request.getYoutube());
    }

    @Test
    public void paramConstructor_withEmptyStrings_storesEmptyStrings() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest(
                null, null, null, null, null,
                "", "", ""
        );

        assertEquals("", request.getInstagram());
        assertEquals("", request.getTiktok());
        assertEquals("", request.getYoutube());
    }

    // Setters -------------------------------------------------------------

    @Test
    public void setName_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setName("Carmen");
        assertEquals("Carmen", request.getName());
    }

    @Test
    public void setPhoneNumber_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setPhoneNumber("600123456");
        assertEquals("600123456", request.getPhoneNumber());
    }

    @Test
    public void setDescription_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setDescription("Nueva bio");
        assertEquals("Nueva bio", request.getDescription());
    }

    @Test
    public void setArtisticName_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setArtisticName("CarmenV");
        assertEquals("CarmenV", request.getArtisticName());
    }

    @Test
    public void setInterests_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        List<String> interests = Arrays.asList("Fitness", "Cocina");
        request.setInterests(interests);
        assertEquals(interests, request.getInterests());
    }

    @Test
    public void setInstagram_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setInstagram("@carmenV");
        assertEquals("@carmenV", request.getInstagram());
    }

    @Test
    public void setTiktok_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setTiktok("@carmenTT");
        assertEquals("@carmenTT", request.getTiktok());
    }

    @Test
    public void setYoutube_updatesValue() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setYoutube("CarmenChannel");
        assertEquals("CarmenChannel", request.getYoutube());
    }

    // Semántica de campos vacíos --------------------------------------

    @Test
    public void setInstagram_withEmptyString_storesEmptyString() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setInstagram("");
        assertEquals("", request.getInstagram());
        assertNotNull(request.getInstagram());
    }

    @Test
    public void setTiktok_withEmptyString_storesEmptyString() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setTiktok("");
        assertEquals("", request.getTiktok());
        assertNotNull(request.getTiktok());
    }

    @Test
    public void setYoutube_withEmptyString_storesEmptyString() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest();
        request.setYoutube("");
        assertEquals("", request.getYoutube());
        assertNotNull(request.getYoutube());
    }
}
