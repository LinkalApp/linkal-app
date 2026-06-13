package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.api.MatchApiService;
import es.miw.tfm.linkal.models.responses.MatchResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MatchRepositoryTest {

    @Mock private MatchApiService mockApiService;
    @Mock private Call<MatchResponse> mockCreateCall;
    @Mock private Call<MatchResponse> mockFindCall;
    @Mock private Call<MatchResponse> mockBusinessCall;
    @Mock private Call<List<MatchResponse>> mockPendingCall;
    @Mock private Call<List<MatchResponse>> mockCompletedCall;
    @Mock private MutableLiveData<MatchResponse> resultLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<List<MatchResponse>> pendingLiveData;
    @Mock private MutableLiveData<List<MatchResponse>> completedLiveData;

    @Mock private MutableLiveData<Boolean> loadingLiveData;
    @Mock private MutableLiveData<Boolean> notFoundLiveData;
    @Mock private ResponseBody mockErrorBody;

    private MatchRepository repository;

    @Before
    public void setUp() {
        repository = new MatchRepository(mockApiService);
        doReturn(mockCreateCall).when(mockApiService).createByInfluencer(anyString(), anyString());
        doReturn(mockFindCall).when(mockApiService).findByInfluencer(anyString(), anyString());
        doReturn(mockBusinessCall).when(mockApiService).createByBusiness(anyString(), anyString(), anyString());
        doReturn(mockPendingCall).when(mockApiService).getPending(anyString());
        doReturn(mockCompletedCall).when(mockApiService).getCompleted(anyString());
    }

    // Singleton -----------------------------------------------------------------------

    @Test
    public void getInstance_returnsSameInstance() {
        MatchRepository i1 = MatchRepository.getInstance();
        MatchRepository i2 = MatchRepository.getInstance();
        assertSame(i1, i2);
    }

    // createByInfluencer: llamada a API ------------------------------------------------------------

    @Test
    public void createByInfluencer_callsApiWithTokenAndCampaignId() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());

        repository.createByInfluencer("Bearer token", "campaign-id", resultLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).createByInfluencer("Bearer token", "campaign-id");
    }

    @Test
    public void createByInfluencer_callsEnqueue() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());

        repository.createByInfluencer("Bearer token", "campaign-id", resultLiveData, errorLiveData, loadingLiveData);

        verify(mockCreateCall).enqueue(any());
    }

    // createByInfluencer: respuesta exitosa -------------------------------------------------------------

    @Test
    public void createByInfluencer_onSuccess_postsMatchToResult() {
        MatchResponse match = buildMatchResponse("PENDING");

        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockCreateCall, Response.success(match));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.createByInfluencer("Bearer token", "campaign-id", resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(match);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void createByInfluencer_onSuccessCompleted_postsCompletedMatch() {
        MatchResponse match = buildMatchResponse("COMPLETED");

        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockCreateCall, Response.success(match));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.createByInfluencer("Bearer token", "campaign-id", resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(match);
        verify(loadingLiveData).postValue(false);
    }

    // createByInfluencer: error HTTP ------------------------------------------------

    @Test
    public void createByInfluencer_on409_postsErrorWithCode() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(409, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.createByInfluencer("Bearer token", "campaign-id", resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("409"));
        verify(resultLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void createByInfluencer_on401_postsErrorMessage() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.createByInfluencer("Bearer expired", "campaign-id", resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
    }

    // createByInfluencer: fallo de red --------------------------------------------------

    @Test
    public void createByInfluencer_onNetworkFailure_postsErrorMessage() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onFailure(mockCreateCall, new RuntimeException("timeout"));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.createByInfluencer("Bearer token", "campaign-id", resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(resultLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // createByBusiness: llamada a API ------------------------------------------------------

    @Test
    public void createByBusiness_callsApiWithCorrectParams() {
        doAnswer(inv -> null).when(mockBusinessCall).enqueue(any());

        repository.createByBusiness("Bearer token", "influencer-id", "campaign-id",
                resultLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).createByBusiness("Bearer token", "influencer-id", "campaign-id");
    }

    @Test
    public void createByBusiness_callsEnqueue() {
        doAnswer(inv -> null).when(mockBusinessCall).enqueue(any());

        repository.createByBusiness("Bearer token", "influencer-id", "campaign-id",
                resultLiveData, errorLiveData, loadingLiveData);

        verify(mockBusinessCall).enqueue(any());
    }

    // createByBusiness: respuesta exitosa -------------------------------------------------------------

    @Test
    public void createByBusiness_onSuccess_postsMatchToResult() {
        MatchResponse match = buildMatchResponse("PENDING");

        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockBusinessCall, Response.success(match));
            return null;
        }).when(mockBusinessCall).enqueue(any());

        repository.createByBusiness("Bearer token", "influencer-id", "campaign-id",
                resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(match);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void createByBusiness_onSuccessCompleted_postsMutualMatch() {
        MatchResponse match = buildMatchResponse("COMPLETED");

        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockBusinessCall, Response.success(match));
            return null;
        }).when(mockBusinessCall).enqueue(any());

        repository.createByBusiness("Bearer token", "influencer-id", "campaign-id",
                resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(match);
        verify(loadingLiveData).postValue(false);
    }

    // createByBusiness: error HTTP -------------------------------------------------------------

    @Test
    public void createByBusiness_on409_postsErrorWithCode() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockBusinessCall, Response.error(409, mockErrorBody));
            return null;
        }).when(mockBusinessCall).enqueue(any());

        repository.createByBusiness("Bearer token", "influencer-id", "campaign-id",
                resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("409"));
        verify(resultLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void createByBusiness_on403_postsErrorMessage() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockBusinessCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockBusinessCall).enqueue(any());

        repository.createByBusiness("Bearer token", "influencer-id", "campaign-id",
                resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
    }

    // createByBusiness: fallo de red -----------------------------------------------------

    @Test
    public void createByBusiness_onNetworkFailure_postsErrorMessage() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onFailure(mockBusinessCall, new RuntimeException("timeout"));
            return null;
        }).when(mockBusinessCall).enqueue(any());

        repository.createByBusiness("Bearer token", "influencer-id", "campaign-id",
                resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(resultLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    //  findByInfluencer: llamada a API ----------------------------------------------

    @Test
    public void findByInfluencer_callsApiWithTokenAndCampaignId() {
        doAnswer(inv -> null).when(mockFindCall).enqueue(any());

        repository.findByInfluencer("Bearer token", "campaign-id", resultLiveData, notFoundLiveData);

        verify(mockApiService).findByInfluencer("Bearer token", "campaign-id");
    }

    // findByInfluencer: respuesta exitosa -----------------------------------------------

    @Test
    public void findByInfluencer_onSuccess_postsMatchToResult() {
        MatchResponse match = buildMatchResponse("PENDING");

        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockFindCall, Response.success(match));
            return null;
        }).when(mockFindCall).enqueue(any());

        repository.findByInfluencer("Bearer token", "campaign-id", resultLiveData, notFoundLiveData);

        verify(resultLiveData).postValue(match);
        verify(notFoundLiveData, never()).postValue(any());
    }

    // findByInfluencer: no encontrado -------------------------------------------

    @Test
    public void findByInfluencer_on404_postsNotFound() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onResponse(mockFindCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockFindCall).enqueue(any());

        repository.findByInfluencer("Bearer token", "campaign-id", resultLiveData, notFoundLiveData);

        verify(notFoundLiveData).postValue(true);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void findByInfluencer_onNetworkFailure_postsNotFound() {
        doAnswer(inv -> {
            Callback<MatchResponse> cb = inv.getArgument(0);
            cb.onFailure(mockFindCall, new RuntimeException("sin conexión"));
            return null;
        }).when(mockFindCall).enqueue(any());

        repository.findByInfluencer("Bearer token", "campaign-id", resultLiveData, notFoundLiveData);

        verify(notFoundLiveData).postValue(true);
        verify(resultLiveData, never()).postValue(any());
    }

    // getPending: llamada a API -----------------------------------------------

    @Test
    public void getPending_callsApiWithToken() {
        doAnswer(inv -> null).when(mockPendingCall).enqueue(any());

        repository.getPending("Bearer token", pendingLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).getPending("Bearer token");
    }

    @Test
    public void getPending_setsLoadingTrue() {
        doAnswer(inv -> null).when(mockPendingCall).enqueue(any());

        repository.getPending("Bearer token", pendingLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    // getPending: respuesta exitosa ------------------------------------------

    @Test
    public void getPending_onSuccess_postsListToResult() {
        java.util.List<MatchResponse> matches = java.util.Arrays.asList(
                buildMatchResponse("PENDING"), buildMatchResponse("PENDING"));

        doAnswer(inv -> {
            Callback<java.util.List<MatchResponse>> cb = inv.getArgument(0);
            cb.onResponse(mockPendingCall, Response.success(matches));
            return null;
        }).when(mockPendingCall).enqueue(any());

        repository.getPending("Bearer token", pendingLiveData, errorLiveData, loadingLiveData);

        verify(pendingLiveData).postValue(matches);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void getPending_onSuccess_postsEmptyList() {
        java.util.List<MatchResponse> empty = java.util.Collections.emptyList();

        doAnswer(inv -> {
            Callback<java.util.List<MatchResponse>> cb = inv.getArgument(0);
            cb.onResponse(mockPendingCall, Response.success(empty));
            return null;
        }).when(mockPendingCall).enqueue(any());

        repository.getPending("Bearer token", pendingLiveData, errorLiveData, loadingLiveData);

        verify(pendingLiveData).postValue(empty);
        verify(loadingLiveData).postValue(false);
    }

    // getPending: error HTTP -----------------------------------------------------

    @Test
    public void getPending_on401_postsErrorMessage() {
        doAnswer(inv -> {
            Callback<java.util.List<MatchResponse>> cb = inv.getArgument(0);
            cb.onResponse(mockPendingCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockPendingCall).enqueue(any());

        repository.getPending("Bearer expired", pendingLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(pendingLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // getPending: fallo de red --------------------------------------------------------

    @Test
    public void getPending_onNetworkFailure_postsErrorMessage() {
        doAnswer(inv -> {
            Callback<java.util.List<MatchResponse>> cb = inv.getArgument(0);
            cb.onFailure(mockPendingCall, new RuntimeException("sin red"));
            return null;
        }).when(mockPendingCall).enqueue(any());

        repository.getPending("Bearer token", pendingLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("sin red"));
        verify(pendingLiveData, never()).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // getCompleted ---------------------------------------------------------------------

    @Test
    public void getCompleted_callsApiWithToken() {
        doAnswer(inv -> null).when(mockCompletedCall).enqueue(any());
        repository.getCompleted("Bearer token", completedLiveData, errorLiveData, loadingLiveData);
        verify(mockApiService).getCompleted("Bearer token");
    }

    @Test
    public void getCompleted_setsLoadingTrue() {
        doAnswer(inv -> null).when(mockCompletedCall).enqueue(any());
        repository.getCompleted("Bearer token", completedLiveData, errorLiveData, loadingLiveData);
        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void getCompleted_onSuccess_postsListToResult() {
        List<MatchResponse> matches = Arrays.asList(buildMatchResponse("COMPLETED"), buildMatchResponse("COMPLETED"));
        doAnswer(inv -> { ((Callback<List<MatchResponse>>) inv.getArgument(0))
                .onResponse(mockCompletedCall, Response.success(matches)); return null; })
                .when(mockCompletedCall).enqueue(any());
        repository.getCompleted("Bearer token", completedLiveData, errorLiveData, loadingLiveData);
        verify(completedLiveData).postValue(matches);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void getCompleted_onSuccess_postsEmptyList() {
        List<MatchResponse> empty = Collections.emptyList();
        doAnswer(inv -> { ((Callback<List<MatchResponse>>) inv.getArgument(0))
                .onResponse(mockCompletedCall, Response.success(empty)); return null; })
                .when(mockCompletedCall).enqueue(any());
        repository.getCompleted("Bearer token", completedLiveData, errorLiveData, loadingLiveData);
        verify(completedLiveData).postValue(empty);
    }

    @Test
    public void getCompleted_on401_postsErrorMessage() {
        doAnswer(inv -> { ((Callback<List<MatchResponse>>) inv.getArgument(0))
                .onResponse(mockCompletedCall, Response.error(401, mockErrorBody)); return null; })
                .when(mockCompletedCall).enqueue(any());
        repository.getCompleted("Bearer expired", completedLiveData, errorLiveData, loadingLiveData);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(completedLiveData, never()).postValue(any());
    }

    @Test
    public void getCompleted_onNetworkFailure_postsErrorMessage() {
        doAnswer(inv -> { ((Callback<List<MatchResponse>>) inv.getArgument(0))
                .onFailure(mockCompletedCall, new RuntimeException("sin red")); return null; })
                .when(mockCompletedCall).enqueue(any());
        repository.getCompleted("Bearer token", completedLiveData, errorLiveData, loadingLiveData);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("sin red"));
        verify(completedLiveData, never()).postValue(any());
    }

    // helpers ----------------------------------------------------------

    private MatchResponse buildMatchResponse(String status) {
        MatchResponse m = new MatchResponse();
        m.setId("match-uuid-001");
        m.setStatus(status);
        m.setCampaignId("campaign-uuid-001");
        m.setInfluencerId("influencer-uuid-001");
        m.setCreatedAt("2026-06-01T10:00:00");
        return m;
    }
}