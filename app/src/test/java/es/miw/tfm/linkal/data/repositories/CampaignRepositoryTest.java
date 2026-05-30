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
import es.miw.tfm.linkal.models.responses.CampaignResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CampaignRepositoryTest {

    @Mock private CampaignApiService mockApiService;
    @Mock private Call<CampaignResponse> mockCreateCall;
    @Mock private ResponseBody mockErrorBody;
    @Mock private MutableLiveData<CampaignResponse> resultLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;

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

    // helpers -------------------------------------------------------------------

    private CampaignResponse buildCampaignResponse() {
        CampaignResponse response = new CampaignResponse();
        response.setId("abc-123");
        response.setTitle("Campaña Verano");
        response.setDescription("Descripción de la campaña");
        response.setStatus("OPEN");
        return response;
    }
}

