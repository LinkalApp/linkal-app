package es.miw.tfm.linkal.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.repositories.EvaluationRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EvaluationViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private EvaluationRepository mockEvaluationRepository;

    private EvaluationViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new EvaluationViewModel(mockEvaluationRepository);
    }

    // Estado inicial ------------------------------------------------------------------

    @Test
    public void evaluationResult_initialValue_isNull() {
        assertNull(viewModel.getEvaluationResult().getValue());
    }

    @Test
    public void errorMessage_initialValue_isNull() {
        assertNull(viewModel.getErrorMessage().getValue());
    }

    @Test
    public void isLoading_initialValue_isFalse() {
        assertFalse(Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));
    }

    // Getters LiveData ----------------------------------------------------------------

    @Test
    public void getEvaluationResult_returnsLiveData() {
        assertNotNull(viewModel.getEvaluationResult());
    }

    @Test
    public void getErrorMessage_returnsLiveData() {
        assertNotNull(viewModel.getErrorMessage());
    }

    @Test
    public void getIsLoading_returnsLiveData() {
        assertNotNull(viewModel.getIsLoading());
    }

    // create ---------------------------------------------------------------------------

    @Test
    public void create_delegatesToRepository() {
        viewModel.create("Bearer token", "match-id-123", 5);

        verify(mockEvaluationRepository).create(
                eq("Bearer token"), eq("match-id-123"), any(), any(), any(), any());
    }

    @Test
    public void create_withDifferentToken_passesItToRepository() {
        viewModel.create("Bearer other-token", "match-id-123", 5);

        verify(mockEvaluationRepository).create(
                eq("Bearer other-token"), any(), any(), any(), any(), any());
    }

    @Test
    public void create_withDifferentMatchId_passesItToRepository() {
        viewModel.create("Bearer token", "other-match-id", 5);

        verify(mockEvaluationRepository).create(
                any(), eq("other-match-id"), any(), any(), any(), any());
    }

    @Test
    public void create_withMinScore_delegatesToRepository() {
        viewModel.create("Bearer token", "match-id", 1);

        verify(mockEvaluationRepository).create(
                any(), any(), any(), any(), any(), any());
    }

    @Test
    public void create_withMaxScore_delegatesToRepository() {
        viewModel.create("Bearer token", "match-id", 5);

        verify(mockEvaluationRepository).create(
                any(), any(), any(), any(), any(), any());
    }
}