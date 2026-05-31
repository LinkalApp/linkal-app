package es.miw.tfm.linkal.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.repositories.BusinessRepository;
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
    @Mock
    BusinessRepository mockBusinessRepository;

    private CampaignViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CampaignViewModel(mockRepository, mockBusinessRepository);
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
    public void campaigns_initialValue_isNull() {
        assertNull(viewModel.getCampaigns().getValue());
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
    public void getCampaigns_returnsLiveData() {
        assertNotNull(viewModel.getCampaigns());
    }

    @Test
    public void getErrorMessage_returnsLiveData() {
        assertNotNull(viewModel.getError());
    }

    @Test
    public void getError_returnsSameLiveDataAsGetErrorMessage() {
        assertSame(viewModel.getError(), viewModel.getError());
    }


    // create -------------------------------------------------------------
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

    // loadByBusiness --------------------------------------------------------------

    @Test
    public void loadByBusiness_delegatesToBusinessRepository() {
        viewModel.loadByBusiness("Bearer token", "business-id-123");

        verify(mockBusinessRepository).getCampaigns(eq("Bearer token"), eq("business-id-123"), any(), any(), any());
    }

    @Test
    public void loadByBusiness_withDifferentToken_passesItToBusinessRepository() {
        viewModel.loadByBusiness("Bearer other-token", "business-id-123");

        verify(mockBusinessRepository).getCampaigns(eq("Bearer other-token"), eq("business-id-123"), any(), any(), any());
    }

    @Test
    public void loadByBusiness_withDifferentBusinessId_passesItToBusinessRepository() {
        viewModel.loadByBusiness("Bearer token", "other-business-id");

        verify(mockBusinessRepository).getCampaigns(eq("Bearer token"), eq("other-business-id"), any(), any(), any());
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
