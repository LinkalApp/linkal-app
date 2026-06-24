package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.api.EvaluationApiService;
import es.miw.tfm.linkal.models.requests.EvaluationRequest;
import es.miw.tfm.linkal.models.responses.EvaluationResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EvaluationRepositoryTest {

    @Mock
    private EvaluationApiService mockApiService;
    @Mock
    private Call<EvaluationResponse> mockCreateCall;
    @Mock
    private MutableLiveData<EvaluationResponse> resultLiveData;
    @Mock
    private MutableLiveData<String> errorLiveData;
    @Mock
    private MutableLiveData<Boolean> loadingLiveData;
    @Mock
    private ResponseBody mockErrorBody;

    private EvaluationRepository repository;

    @Before
    public void setUp() {
        repository = new EvaluationRepository(mockApiService);
        when(mockApiService.create(anyString(), anyString(), any())).thenReturn(mockCreateCall);
    }

    // Singleton ---------------------------------------------------------------------

    @Test
    public void getInstance_returnsSameInstance() {
        EvaluationRepository i1 = EvaluationRepository.getInstance();
        EvaluationRepository i2 = EvaluationRepository.getInstance();
        assertSame(i1, i2);
    }

    // create -----------------------------------------------------------------------

    @Test
    public void create_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void create_callsApiServiceWithCorrectParams() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());
        EvaluationRequest request = buildRequest(5);

        repository.create("Bearer token", "match-id-123", request, resultLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).create(eq("Bearer token"), eq("match-id-123"), eq(request));
    }

    @Test
    public void create_callsEnqueueOnCall() {
        doAnswer(inv -> null).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(4), resultLiveData, errorLiveData, loadingLiveData);

        verify(mockCreateCall).enqueue(any());
    }

    @Test
    public void create_onSuccess_postsResponseBodyToResult() {
        EvaluationResponse responseBody = buildResponse("eval-1", 5);

        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.success(responseBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(responseBody);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void create_onSuccessWithNullBody_postsNullToResult() {
        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.success(null));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        verify(resultLiveData).postValue(null);
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void create_on401Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer expired", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void create_on403Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void create_on404Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("404"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void create_on409Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockCreateCall, Response.error(409, mockErrorBody));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("409"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void create_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockCreateCall, new RuntimeException("timeout"));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(resultLiveData, never()).postValue(any());
    }

    @Test
    public void create_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<EvaluationResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockCreateCall, new RuntimeException((String) null));
            return null;
        }).when(mockCreateCall).enqueue(any());

        repository.create("Bearer token", "match-id", buildRequest(5), resultLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // helpers ------------------------------------------------------------------

    private EvaluationRequest buildRequest(int score) {
        return new EvaluationRequest(score);
    }

    private EvaluationResponse buildResponse(String id, int score) {
        EvaluationResponse response = new EvaluationResponse();
        response.setId(id);
        response.setScore(score);
        response.setValuedUserId("influencer-uuid");
        response.setMatchId("match-uuid");
        return response;
    }
}