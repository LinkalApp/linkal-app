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
import es.miw.tfm.linkal.models.requests.UpdateCampaignRequest;
import es.miw.tfm.linkal.models.requests.UpdateCampaignRequestTest;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CampaignViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private CampaignRepository mockCampaignRepository;
    @Mock
    private BusinessRepository mockBusinessRepository;

    private CampaignViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new CampaignViewModel(mockCampaignRepository, mockBusinessRepository);
    }

    @Test
    public void isLoading_initialValue_isFalse() {
        Boolean value = viewModel.getIsLoading().getValue();
        assertNotNull(value);
        assertFalse(value);
    }

    @Test
    public void createResult_initialValue_isNull() { assertNull(viewModel.getCreateResult().getValue()); }
    @Test
    public void updateResult_initialValue_isNull() { assertNull(viewModel.getUpdateResult().getValue()); }
    @Test
    public void deleteResult_initialValue_isNull() { assertNull(viewModel.getDeleteResult().getValue()); }
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
    public void getUpdateResult_returnsLiveData() {
        assertNotNull(viewModel.getUpdateResult());
    }
    @Test
    public void getDeleteResult_returnsLiveData() { assertNotNull(viewModel.getDeleteResult());}
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

        verify(mockCampaignRepository).create(eq("Bearer token"), eq(request), any(), any(), any());
    }

    @Test
    public void create_withDifferentToken_passesItToRepository() {
        CreateCampaignRequest request = buildCreateRequest();

        viewModel.create("Bearer other-token", request);

        verify(mockCampaignRepository).create(eq("Bearer other-token"), eq(request), any(), any(), any());
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

    // update ---------------------------------------------------------------------

    @Test
    public void update_delegatesToCampaignRepository() {
        UpdateCampaignRequest request = buildUpdateRequest();

        viewModel.update("Bearer token", "campaign-id-123", request);

        verify(mockCampaignRepository).update(eq("Bearer token"), eq("campaign-id-123"), eq(request), any(), any(), any());
    }

    @Test
    public void update_withDifferentToken_passesItToRepository() {
        UpdateCampaignRequest request = buildUpdateRequest();

        viewModel.update("Bearer other-token", "campaign-id-123", request);

        verify(mockCampaignRepository).update(eq("Bearer other-token"), eq("campaign-id-123"), eq(request), any(), any(), any());
    }

    // delete ---------------------------------------------------------------------

    @Test
    public void delete_delegatesToCampaignRepository() {
        viewModel.delete("Bearer token", "campaign-id-123");

        verify(mockCampaignRepository).delete(eq("Bearer token"), eq("campaign-id-123"), any(), any(), any());
    }

    @Test
    public void delete_withDifferentToken_passesItToRepository() {
        viewModel.delete("Bearer other-token", "campaign-id-123");

        verify(mockCampaignRepository).delete(eq("Bearer other-token"), eq("campaign-id-123"), any(), any(), any());
    }

    @Test
    public void delete_withDifferentCampaignId_passesItToRepository() {
        viewModel.delete("Bearer token", "other-campaign-id");

        verify(mockCampaignRepository).delete(eq("Bearer token"), eq("other-campaign-id"), any(), any(), any());
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

    private UpdateCampaignRequest buildUpdateRequest() {
        return new UpdateCampaignRequest(
                "Título actualizado", "Nueva descripción", "Nuevos requisitos",
                "Nueva recompensa", "Nuevo objetivo", "IN_PROGRESS");
    }
}
