package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.api.CampaignApiService;
import es.miw.tfm.linkal.models.requests.CreateCampaignRequest;
import es.miw.tfm.linkal.models.requests.UpdateCampaignRequest;
import es.miw.tfm.linkal.models.responses.CampaignResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CampaignRepositoryTest {

    @Mock private CampaignApiService mockApiService;
    @Mock private Call<CampaignResponse> mockCreateCall;
    @Mock private Call<CampaignResponse> mockUpdateCall;
    @Mock private Call<Void> mockDeleteCall;
    @Mock private Call<List<CampaignResponse>> mockGetOpenCall;
    @Mock private Call<List<CampaignResponse>> mockGetOpenByFiltersCall;
    @Mock private ResponseBody mockErrorBody;
    @Mock private MutableLiveData<CampaignResponse> resultLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;
    @Mock private MutableLiveData<List<CampaignResponse>> openCampaignsLiveData;

    private CampaignRepository repository;
    private CreateCampaignRequest createRequest;

    @Before
    public void setUp() {
        repository = new CampaignRepository(mockApiService);
        createRequest = new CreateCampaignRequest(
                "Campaña Verano",
                "Descripción de la campaña",
                "500 seguidores mínimo",
                "Descuento 20%",
                "Aumentar ventas"
        );
        when(mockApiService.create(anyString(), any())).thenReturn(mockCreateCall);
        when(mockApiService.update(anyString(), anyString(), any())).thenReturn(mockUpdateCall);
        when(mockApiService.delete(anyString(), anyString())).thenReturn(mockDeleteCall);
        when(mockApiService.getOpenCampaigns(anyString())).thenReturn(mockGetOpenCall);
        when(mockApiService.getOpenCampaignsByFilters(anyString(), any(), any())).thenReturn(mockGetOpenByFiltersCall);
    }

    // Singleton ------------------------------------------------------------------

    @Test
    public void getInstance_returnsSameInstance() {
        CampaignRepository i1 = CampaignRepository.getInstance();
        CampaignRepository i2 = CampaignRepository.getInstance();
        assertSame(i1, i2);
    }

    // create: loading -----------------------------------------------------------

    @Test
    public void create_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void create_callsApiServiceWithToken() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).create(eq("Bearer token"), eq(createRequest));
    }

    @Test
    public void create_callsEnqueueOnCall() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        verify(mockCreateCall).enqueue(any());
    }

    // create: respuesta exitosa -----------------------------------------------

    @Test
    public void create_onSuccess_postsResponseBodyToResult() {
        CampaignResponse responseBody = buildCampaignResponse();

        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.success(responseBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(responseBody);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void create_onSuccessWithNullBody_postsNullToResult() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.success(null));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(null);
        verify(loadingLiveData).postValue(false);
    }

    //  create: error HTTP ---------------------------------------------------------

    @Test
    public void create_on401Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer expired", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 401", captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void create_on403Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 403", captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void create_on500Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(500, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 500", captor.getValue().contains("500"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void create_on404Response_doesNotPostResult() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    //  create: fallo de red -------------------------------------------------------

    @Test
    public void create_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockCreateCall, new RuntimeException("timeout"));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el texto del error", captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void create_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockCreateCall, new RuntimeException((String) null));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", createRequest, resultLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // update: loading ---------------------------------------------------------------

    @Test
    public void update_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void update_callsApiServiceWithTokenAndId() {
        doAnswer(inv -> null).when(mockUpdateCall).enqueue(any());
        UpdateCampaignRequest request = buildUpdateRequest();

        repository.update("Bearer token", "campaign-id-123", request, resultLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).update(eq("Bearer token"), eq("campaign-id-123"), eq(request));
    }

    @Test
    public void update_callsEnqueueOnCall() {
        doAnswer(inv -> null).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        verify(mockUpdateCall).enqueue(any());
    }

    // update: respuesta exitosa ----------------------------------------------------------------

    @Test
    public void update_onSuccess_postsUpdatedCampaignToResult() {
        CampaignResponse updated = buildCampaignResponse();
        updated.setStatus("IN_PROGRESS");

        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.success(updated));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(updated);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void update_onSuccessWithNullBody_postsNullToResult() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.success(null));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(null);
        verify(loadingLiveData).postValue(false);
    }

    // update: error HTTP -------------------------------------------------------------

    @Test
    public void update_on401Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer expired", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 401", captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void update_on403Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 403", captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void update_on404Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 404", captor.getValue().contains("404"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void update_on500Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.error(500, mockErrorBody));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 500", captor.getValue().contains("500"));
        verify(loadingLiveData).postValue(false);
    }

    // update: fallo de red -----------------------------------------------------------------

    @Test
    public void update_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockUpdateCall, new RuntimeException("timeout"));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el texto del error", captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void update_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<CampaignResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockUpdateCall, new RuntimeException((String) null));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.update("Bearer token", "campaign-id-123", buildUpdateRequest(), resultLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // delete: loading ---------------------------------------------------------------------------------

    @Test
    public void delete_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockDeleteCall).enqueue(any());

        repository.delete("Bearer token", "campaign-id-123", loadingLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void delete_callsApiServiceWithTokenAndId() {
        doAnswer(inv -> null).when(mockDeleteCall).enqueue(any());

        repository.delete("Bearer token", "campaign-id-123", loadingLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).delete(eq("Bearer token"), eq("campaign-id-123"));
    }

    @Test
    public void delete_callsEnqueueOnCall() {
        doAnswer(inv -> null).when(mockDeleteCall).enqueue(any());

        repository.delete("Bearer token", "campaign-id-123", loadingLiveData, errorLiveData, loadingLiveData);

        verify(mockDeleteCall).enqueue(any());
    }

    // delete: respuesta exitosa -----------------------------------------------------------------------------------

    @Test
    public void delete_onSuccess_postsTrueToResult() {
        MutableLiveData<Boolean> deleteResult = mock(MutableLiveData.class);

        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockDeleteCall, Response.success(null));
            return null;
        }).when(mockDeleteCall).enqueue(any());

        repository.delete("Bearer token", "campaign-id-123", deleteResult, errorLiveData, loadingLiveData);

        verify(deleteResult).postValue(true);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    // delete: error HTTP -----------------------------------------------------------------

    @Test
    public void delete_on403Response_postsErrorMessage() {
        MutableLiveData<Boolean> deleteResult = mock(MutableLiveData.class);

        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockDeleteCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockDeleteCall).enqueue(any());

        repository.delete("Bearer token", "campaign-id-123", deleteResult, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("Debe contener el código 403", captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
        verify(deleteResult, never()).postValue(any());
    }

    @Test
    public void delete_on404Response_postsErrorMessage() {
        MutableLiveData<Boolean> deleteResult = mock(MutableLiveData.class);

        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockDeleteCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockDeleteCall).enqueue(any());

        repository.delete("Bearer token", "campaign-id-123", deleteResult, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("404"));
        verify(loadingLiveData).postValue(false);
    }

    // delete: fallo de red --------------------------------------------------------------

    @Test
    public void delete_onNetworkFailure_postsConnectionErrorMessage() {
        MutableLiveData<Boolean> deleteResult = mock(MutableLiveData.class);

        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockDeleteCall, new RuntimeException("timeout"));
            return null;
        }).when(mockDeleteCall).enqueue(any());

        repository.delete("Bearer token", "campaign-id-123", deleteResult, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(deleteResult, never()).postValue(any());
    }

    // ─── getOpenCampaigns ─────────────────────────────────────────────────────

    @Test
    public void getOpenCampaigns_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockGetOpenCall).enqueue(any());

        repository.getOpenCampaigns("Bearer token", openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void getOpenCampaigns_callsApiServiceWithToken() {
        doAnswer(inv -> null).when(mockGetOpenCall).enqueue(any());

        repository.getOpenCampaigns("Bearer token", openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).getOpenCampaigns(eq("Bearer token"));
    }

    @Test
    public void getOpenCampaigns_onSuccess_postsListToResult() {
        List<CampaignResponse> list = Arrays.asList(buildCampaignResponse(), buildCampaignResponse());

        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenCall, Response.success(list));
            return null;
        }).when(mockGetOpenCall).enqueue(any());

        repository.getOpenCampaigns("Bearer token", openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(openCampaignsLiveData).postValue(list);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void getOpenCampaigns_onSuccessEmptyList_postsEmptyList() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenCall, Response.success(Arrays.asList()));
            return null;
        }).when(mockGetOpenCall).enqueue(any());

        repository.getOpenCampaigns("Bearer token", openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(openCampaignsLiveData).postValue(Arrays.asList());
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void getOpenCampaigns_on401Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockGetOpenCall).enqueue(any());

        repository.getOpenCampaigns("Bearer expired", openCampaignsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(openCampaignsLiveData, never()).postValue(any());
    }

    @Test
    public void getOpenCampaigns_on403Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockGetOpenCall).enqueue(any());

        repository.getOpenCampaigns("Bearer token", openCampaignsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void getOpenCampaigns_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onFailure(mockGetOpenCall, new RuntimeException("timeout"));
            return null;
        }).when(mockGetOpenCall).enqueue(any());

        repository.getOpenCampaigns("Bearer token", openCampaignsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(openCampaignsLiveData, never()).postValue(any());
    }

    // getOpenCampaignsByFilters -------------------------------------------------------------------------

    @Test
    public void getOpenCampaignsByFilters_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockGetOpenByFiltersCall).enqueue(any());

        repository.getOpenCampaignsByFilters("Bearer token", "Tecnología", "Madrid",
                openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void getOpenCampaignsByFilters_callsApiServiceWithTokenCategoryAndProvince() {
        doAnswer(inv -> null).when(mockGetOpenByFiltersCall).enqueue(any());

        repository.getOpenCampaignsByFilters("Bearer token", "Tecnología", "Madrid",
                openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).getOpenCampaignsByFilters(eq("Bearer token"), eq("Tecnología"), eq("Madrid"));
    }

    @Test
    public void getOpenCampaignsByFilters_onSuccess_postsFilteredListToResult() {
        List<CampaignResponse> filtered = Arrays.asList(buildCampaignResponse(), buildCampaignResponse());

        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenByFiltersCall, Response.success(filtered));
            return null;
        }).when(mockGetOpenByFiltersCall).enqueue(any());

        repository.getOpenCampaignsByFilters("Bearer token", "Tecnología", "Madrid",
                openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(openCampaignsLiveData).postValue(filtered);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void getOpenCampaignsByFilters_onSuccessEmptyList_postsEmptyList() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenByFiltersCall, Response.success(Arrays.asList()));
            return null;
        }).when(mockGetOpenByFiltersCall).enqueue(any());

        repository.getOpenCampaignsByFilters("Bearer token", "Gaming", null,
                openCampaignsLiveData, errorLiveData, loadingLiveData);

        verify(openCampaignsLiveData).postValue(Arrays.asList());
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void getOpenCampaignsByFilters_on401Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenByFiltersCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockGetOpenByFiltersCall).enqueue(any());

        repository.getOpenCampaignsByFilters("Bearer expired", "Tecnología", "Madrid",
                openCampaignsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(openCampaignsLiveData, never()).postValue(any());
    }

    @Test
    public void getOpenCampaignsByFilters_on403Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onResponse(mockGetOpenByFiltersCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockGetOpenByFiltersCall).enqueue(any());

        repository.getOpenCampaignsByFilters("Bearer token", "Tecnología", "Madrid",
                openCampaignsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void getOpenCampaignsByFilters_onNetworkFailure_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<List<CampaignResponse>> cb = invocation.getArgument(0);
            cb.onFailure(mockGetOpenByFiltersCall, new RuntimeException("timeout"));
            return null;
        }).when(mockGetOpenByFiltersCall).enqueue(any());

        repository.getOpenCampaignsByFilters("Bearer token", "Tecnología", "Madrid",
                openCampaignsLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(openCampaignsLiveData, never()).postValue(any());
    }


    // helpers -------------------------------------------------------------------

    private CampaignResponse buildCampaignResponse() {
        CampaignResponse response = new CampaignResponse();
        response.setId("abc-123");
        response.setTitle("Campaña Verano");
        response.setDescription("Descripción de la campaña");
        response.setStatus("OPEN");
        return response;
    }

    private UpdateCampaignRequest buildUpdateRequest() {
        return new UpdateCampaignRequest(
                "Título actualizado", "Nueva descripción", "Nuevos requisitos",
                "Nueva recompensa", "Nuevo objetivo", "IN_PROGRESS");
    }
}

