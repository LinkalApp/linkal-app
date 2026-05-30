package es.miw.tfm.linkal.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.repositories.CampaignRepository;
import es.miw.tfm.linkal.models.requests.CreateCampaignRequest;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CampaignViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private CampaignRepository mockRepository;

    private CampaignViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CampaignViewModel(mockRepository);
    }

    @Test
    public void isLoading_initialValue_isFalse() {
        Boolean value = viewModel.getIsLoading().getValue();
        assertNotNull(value);
        assertFalse(value);
    }

    @Test
    public void createResult_initialValue_isNull() {
        assertNull(viewModel.getCreateResult().getValue());
    }

    @Test
    public void errorMessage_initialValue_isNull() {
        assertNull(viewModel.getError().getValue());
    }

    @Test
    public void getIsLoading_returnsLiveData() {
        assertNotNull(viewModel.getIsLoading());
    }

    @Test
    public void getCreateResult_returnsLiveData() {
        assertNotNull(viewModel.getCreateResult());
    }

    @Test
    public void getErrorMessage_returnsLiveData() {
        assertNotNull(viewModel.getError());
    }

    @Test
    public void getError_returnsSameLiveDataAsGetErrorMessage() {
        assertSame(viewModel.getError(), viewModel.getError());
    }


    @Test
    public void create_delegatesToRepository() {
        CreateCampaignRequest request = buildCreateRequest();

        viewModel.create("Bearer token", request);

        verify(mockRepository).create(eq("Bearer token"), eq(request), any(), any(), any());
    }

    @Test
    public void create_withDifferentToken_passesItToRepository() {
        CreateCampaignRequest request = buildCreateRequest();

        viewModel.create("Bearer other-token", request);

        verify(mockRepository).create(eq("Bearer other-token"), eq(request), any(), any(), any());
    }

    // helpers ----------------------------------------------------------

    private CreateCampaignRequest buildCreateRequest() {
        return new CreateCampaignRequest(
                "Campaña Verano",
                "Descripción de la campaña",
                "500 seguidores mínimo",
                "Descuento 20%",
                "Aumentar ventas"
        );
    }
}
