package es.miw.tfm.linkal.models.responses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EvaluationResponseTest {
    @Test
    public void defaultConstructor_createsInstanceWithNullFields() {
        EvaluationResponse response = new EvaluationResponse();
        assertNull(response.getId());
        assertNull(response.getScore());
        assertNull(response.getValuedUserId());
        assertNull(response.getMatchId());
    }

    @Test
    public void setId_updatesId() {
        EvaluationResponse response = new EvaluationResponse();
        response.setId("eval-123");
        assertEquals("eval-123", response.getId());
    }

    @Test
    public void setScore_updatesScore() {
        EvaluationResponse response = new EvaluationResponse();
        response.setScore(5);
        assertEquals(Integer.valueOf(5), response.getScore());
    }

    @Test
    public void setValuedUserId_updatesValuedUserId() {
        EvaluationResponse response = new EvaluationResponse();
        response.setValuedUserId("influencer-uuid");
        assertEquals("influencer-uuid", response.getValuedUserId());
    }

    @Test
    public void setMatchId_updatesMatchId() {
        EvaluationResponse response = new EvaluationResponse();
        response.setMatchId("match-uuid");
        assertEquals("match-uuid", response.getMatchId());
    }

    @Test
    public void setAllFields_allGettersReturnCorrectValues() {
        EvaluationResponse response = new EvaluationResponse();
        response.setId("eval-1");
        response.setScore(4);
        response.setValuedUserId("inf-1");
        response.setMatchId("match-1");

        assertEquals("eval-1",  response.getId());
        assertEquals(Integer.valueOf(4), response.getScore());
        assertEquals("inf-1",   response.getValuedUserId());
        assertEquals("match-1", response.getMatchId());
    }
}
