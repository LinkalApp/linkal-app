package es.miw.tfm.linkal.data.repositories;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import es.miw.tfm.linkal.data.api.BusinessApiService;
import es.miw.tfm.linkal.data.api.InfluencerApiService;
import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InfluencerRepositoryTest {

    @Mock private InfluencerApiService     mockApiService;
    @Mock private Call<Void>               mockCall;
    @Mock private ResponseBody             mockErrorBody;
    @Mock private MutableLiveData<Boolean> successLiveData;
    @Mock private MutableLiveData<String>  errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;

    private InfluencerRepository      repository;
    private RegisterInfluencerRequest request;

    @Before
    public void setUp() {
        repository = new InfluencerRepository(mockApiService);
        List<String> interests = Arrays.asList("Moda", "Tecnología");
        request = new RegisterInfluencerRequest(
                "Laura Test", "laura@test.com", "SecurePass1.", "666000000",
                "Bio de prueba", "LauraStyle", interests,
                "@laurastyle", "@lauratiktok", "LauraYT"
        );
        when(mockApiService.register(any())).thenReturn(mockCall);
    }

    // Singleton -------------------------------------------------------------

    @Test
    public void getInstance_returnsSameInstance() {
        InfluencerRepository i1 = InfluencerRepository.getInstance();
        InfluencerRepository i2 = InfluencerRepository.getInstance();
        assertSame(i1, i2);
    }

    // Estado inicial de loading ------------------------------------------------

    @Test
    public void register_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    // Respuesta exitosa ----------------------------------------------------------

    @Test
    public void register_onSuccessfulResponse_postsSuccessTrue() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockCall, Response.success(null));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        verify(successLiveData).postValue(true);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    // Respuesta con error HTTP --------------------------------------------------------

    @Test
    public void register_onErrorResponse_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el código 403",
                captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
        verify(successLiveData, never()).postValue(any());
    }

    @Test
    public void register_on409ConflictResponse_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockCall, Response.error(409, mockErrorBody));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el código 409",
                captor.getValue().contains("409"));
        verify(loadingLiveData).postValue(false);
    }

    // Fallo de red ----------------------------------------------------------

    @Test
    public void register_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockCall, new RuntimeException("No route to host"));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el texto del error",
                captor.getValue().contains("No route to host"));
        verify(loadingLiveData).postValue(false);
        verify(successLiveData, never()).postValue(any());
    }

    @Test
    public void register_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockCall, new RuntimeException((String) null));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }
}
