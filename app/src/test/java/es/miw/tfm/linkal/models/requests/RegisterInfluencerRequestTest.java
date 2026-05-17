package es.miw.tfm.linkal.models.requests;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class RegisterInfluencerRequestTest {

    private RegisterInfluencerRequest request;
    private List<String> interests;

    @Before
    public void setUp() {
        interests = Arrays.asList("Moda", "Tecnología", "Viajes");
        request = new RegisterInfluencerRequest(
                "Laura Influencer",
                "laura@example.com",
                "pass1234",
                "666987654",
                "Soy influencer de moda",
                "LauraStyle",
                interests,
                "@laurastyle",
                "@lauratiktok",
                "LauraYT"
        );
    }

    // CAMPOS HEREDADOS DE RegisterUserRequest -------------------------

    @Test
    public void getName_returnsCorrectValue() {
        assertEquals("Laura Influencer", request.getName());
    }

    @Test
    public void getEmail_returnsCorrectValue() {
        assertEquals("laura@example.com", request.getEmail());
    }

    @Test
    public void getPassword_returnsCorrectValue() {
        assertEquals("pass1234", request.getPassword());
    }

    @Test
    public void getPhoneNumber_returnsCorrectValue() {
        assertEquals("666987654", request.getPhoneNumber());
    }

    @Test
    public void getDescription_returnsCorrectValue() {
        assertEquals("Soy influencer de moda", request.getDescription());
    }

    @Test
    public void getRole_isAlwaysInfluencer() {
        assertEquals("INFLUENCER", request.getRole());
    }

    // CAMPOS PROPIOS DE RegisterInfluencerRequest ------------------------

    @Test
    public void getArtisticName_returnsCorrectValue() {
        assertEquals("LauraStyle", request.getArtisticName());
    }

    @Test
    public void getInstagram_returnsCorrectValue() {
        assertEquals("@laurastyle", request.getInstagram());
    }

    @Test
    public void getTiktok_returnsCorrectValue() {
        assertEquals("@lauratiktok", request.getTiktok());
    }

    @Test
    public void getYoutube_returnsCorrectValue() {
        assertEquals("LauraYT", request.getYoutube());
    }

    // LISTA DE INTERESES -----------------------------------------------

    @Test
    public void getInterests_returnsCorrectList() {
        List<String> result = request.getInterests();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Moda"));
        assertTrue(result.contains("Tecnología"));
        assertTrue(result.contains("Viajes"));
    }

    @Test
    public void getInterests_withEmptyList_returnsEmptyList() {
        RegisterInfluencerRequest emptyInterests = new RegisterInfluencerRequest(
                "Test", "t@t.com", "pass", "123", "desc",
                "ArtName", Collections.emptyList(),
                null, null, null
        );
        assertNotNull(emptyInterests.getInterests());
        assertTrue(emptyInterests.getInterests().isEmpty());
    }

    // HERENCIA -----------------------------------------------------------

    @Test
    public void isInstanceOfRegisterUserRequest() {
        assertTrue(request instanceof RegisterUserRequest);
    }

    @Test
    public void constructor_withNullSocialNetworks_doesNotThrow() {
        RegisterInfluencerRequest noSocials = new RegisterInfluencerRequest(
                "Inf", "i@i.com", "pass", "000", "desc",
                "ArtName", interests,
                null, null, null
        );
        assertNull(noSocials.getInstagram());
        assertNull(noSocials.getTiktok());
        assertNull(noSocials.getYoutube());
        assertEquals("INFLUENCER", noSocials.getRole());
    }
}

