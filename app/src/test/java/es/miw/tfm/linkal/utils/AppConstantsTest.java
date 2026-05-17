package es.miw.tfm.linkal.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppConstantsTest {

    // ROLES -------------------------------------------------------

    @Test
    public void roleInfluencer_isCorrect() {
        assertEquals("INFLUENCER", AppConstants.ROLE_INFLUENCER);
    }

    @Test
    public void roleBusiness_isCorrect() {
        assertEquals("BUSINESS", AppConstants.ROLE_BUSINESS);
    }

    // PROVINCIAS ----------------------------------------------------

    @Test
    public void provinces_notNullAndNotEmpty() {
        assertNotNull(AppConstants.PROVINCES);
        assertTrue(AppConstants.PROVINCES.length > 0);
    }

    @Test
    public void provinces_firstElementIsPlaceholder() {
        assertEquals("Selecciona una provincia", AppConstants.PROVINCES[0]);
    }

    @Test
    public void provinces_containsMadrid() {
        boolean found = false;
        for (String p : AppConstants.PROVINCES) {
            if ("Madrid".equals(p)) { found = true; break; }
        }
        assertTrue("Madrid should be in PROVINCES", found);
    }

    @Test
    public void provinces_containsBarcelona() {
        boolean found = false;
        for (String p : AppConstants.PROVINCES) {
            if ("Barcelona".equals(p)) { found = true; break; }
        }
        assertTrue("Barcelona should be in PROVINCES", found);
    }

    // CATEGORÍAS ---------------------------------------------------

    @Test
    public void categories_notNullAndNotEmpty() {
        assertNotNull(AppConstants.CATEGORIES);
        assertTrue(AppConstants.CATEGORIES.length > 0);
    }

    @Test
    public void categories_firstElementIsPlaceholder() {
        assertEquals("Selecciona una categoría", AppConstants.CATEGORIES[0]);
    }

    @Test
    public void categories_containsOtra() {
        boolean found = false;
        for (String c : AppConstants.CATEGORIES) {
            if ("Otra".equals(c)) { found = true; break; }
        }
        assertTrue("'Otra' should be in CATEGORIES", found);
    }

    //  INTERESES ---------------------------------------------------

    @Test
    public void interests_notNullAndNotEmpty() {
        assertNotNull(AppConstants.INTERESTS);
        assertTrue(AppConstants.INTERESTS.length > 0);
    }

    @Test
    public void interests_containsModa() {
        boolean found = false;
        for (String i : AppConstants.INTERESTS) {
            if ("Moda".equals(i)) { found = true; break; }
        }
        assertTrue("'Moda' should be in INTERESTS", found);
    }

    @Test
    public void interests_containsTecnologia() {
        boolean found = false;
        for (String i : AppConstants.INTERESTS) {
            if ("Tecnología".equals(i)) { found = true; break; }
        }
        assertTrue("'Tecnología' should be in INTERESTS", found);
    }

    @Test
    public void interests_noNullElements() {
        for (String interest : AppConstants.INTERESTS) {
            assertNotNull(interest);
        }
    }
}