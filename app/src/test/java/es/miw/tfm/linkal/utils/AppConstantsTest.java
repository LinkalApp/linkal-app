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

    // STATUS CAMPAIGN OPTIONS ---------------------------------------------------

    @Test
    public void statusCampaignOptions_notNullAndNotEmpty() {
        assertNotNull(AppConstants.STATUS_CAMPAIGN_OPTIONS);
        assertTrue(AppConstants.STATUS_CAMPAIGN_OPTIONS.length > 0);
    }

    @Test
    public void statusCampaignOptions_containsOpen() {
        boolean found = false;
        for (String s : AppConstants.STATUS_CAMPAIGN_OPTIONS) {
            if ("OPEN".equals(s)) { found = true; break; }
        }
        assertTrue("'OPEN' should be in STATUS_CAMPAIGN_OPTIONS", found);
    }

    @Test
    public void statusCampaignOptions_containsInProgress() {
        boolean found = false;
        for (String s : AppConstants.STATUS_CAMPAIGN_OPTIONS) {
            if ("IN_PROGRESS".equals(s)) { found = true; break; }
        }
        assertTrue("'IN_PROGRESS' should be in STATUS_CAMPAIGN_OPTIONS", found);
    }

    @Test
    public void statusCampaignOptions_containsClosed() {
        boolean found = false;
        for (String s : AppConstants.STATUS_CAMPAIGN_OPTIONS) {
            if ("CLOSED".equals(s)) { found = true; break; }
        }
        assertTrue("'CLOSED' should be in STATUS_CAMPAIGN_OPTIONS", found);
    }

    @Test
    public void statusCampaignOptions_hasExactlyThreeValues() {
        assertEquals(3, AppConstants.STATUS_CAMPAIGN_OPTIONS.length);
    }

    @Test
    public void statusCampaignOptions_noNullElements() {
        for (String s : AppConstants.STATUS_CAMPAIGN_OPTIONS) {
            assertNotNull(s);
        }
    }
}