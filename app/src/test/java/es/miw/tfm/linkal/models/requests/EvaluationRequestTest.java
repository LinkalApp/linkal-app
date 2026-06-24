package es.miw.tfm.linkal.models.requests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EvaluationRequestTest {
    @Test
    public void defaultConstructor_createsInstanceWithNullScore() {
        EvaluationRequest request = new EvaluationRequest();
        assertNull(request.getScore());
    }

    @Test
    public void paramConstructor_setsScore() {
        EvaluationRequest request = new EvaluationRequest(5);
        assertEquals(Integer.valueOf(5), request.getScore());
    }

    @Test
    public void setScore_updatesScore() {
        EvaluationRequest request = new EvaluationRequest();
        request.setScore(3);
        assertEquals(Integer.valueOf(3), request.getScore());
    }

    @Test
    public void paramConstructor_withMinScore_setsScore() {
        EvaluationRequest request = new EvaluationRequest(1);
        assertEquals(Integer.valueOf(1), request.getScore());
    }

    @Test
    public void setScore_overwritesPreviousValue() {
        EvaluationRequest request = new EvaluationRequest(4);
        request.setScore(2);
        assertEquals(Integer.valueOf(2), request.getScore());
    }
}
