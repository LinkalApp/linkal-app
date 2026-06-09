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

@RunWith(MockitoJUnitRunner.Silent.class)
public class MatchRepositoryTest {

    @Mock private MatchApiService mockApiService;
    @Mock private Call<MatchResponse> mockCreateCall;
    @Mock private Call<MatchResponse> mockFindCall;
    @Mock private MutableLiveData<MatchResponse> resultLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;
    @Mock private MutableLiveData<Boolean> notFoundLiveData;
    @Mock private ResponseBody mockErrorBody;

    private MatchRepository repository;

    @Before
    public void setUp() {
        repository = new MatchRepository(mockApiService);
        doReturn(mockCreateCall).when(mockApiService).createByInfluencer(anyString(), anyString());
        doReturn(mockFindCall).when(mockApiService).findByInfluencer(anyString(), anyString());
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