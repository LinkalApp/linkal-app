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
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InfluencerRepositoryTest {

    @Mock private InfluencerApiService mockApiService;
    @Mock private Call<Void> mockCall;
    @Mock private Call<InfluencerProfileResponse> mockProfileCall;
    @Mock private Call<InfluencerProfileResponse> mockUpdateCall;

    @Mock private ResponseBody mockErrorBody;
    @Mock private MutableLiveData<Boolean> successLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;
    @Mock private MutableLiveData<InfluencerProfileResponse> profileLiveData;

    private InfluencerRepository repository;
    private RegisterInfluencerRequest request;
    private UpdateInfluencerRequest updateRequest;

    @Before
    public void setUp() {
        repository = new InfluencerRepository(mockApiService);
        List<String> interests = Arrays.asList("Moda", "Tecnología");
        request = new RegisterInfluencerRequest(
                "Laura Test", "laura@test.com", "SecurePass1.", "666000000",
                "Bio de prueba", "LauraStyle", interests,
                "@laurastyle", "@lauratiktok", "LauraYT"
        );
        updateRequest = new UpdateInfluencerRequest(
                "Laura Test", "666000000", "Bio de prueba", "LauraStyle",
                interests, "@laurastyle", "@lauratiktok", "LauraYT"
        );
        when(mockApiService.register(any())).thenReturn(mockCall);
        when(mockApiService.getProfile(anyString())).thenReturn(mockProfileCall);
        when(mockApiService.updateProfile(anyString(), any())).thenReturn(mockUpdateCall);
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

    // getProfile ------------------------------------------------------------

    @Test
    public void getProfile_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer token", profileLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void getProfile_onSuccess_postsProfileData() {
        InfluencerProfileResponse profile = new InfluencerProfileResponse();
        profile.setEmail("user@test.com");

        doAnswer(invocation -> {
            Callback<InfluencerProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockProfileCall, Response.success(profile));
            return null;
        }).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer token", profileLiveData, errorLiveData, loadingLiveData);

        verify(profileLiveData).postValue(profile);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void getProfile_on401Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<InfluencerProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockProfileCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer expired", profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void getProfile_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<InfluencerProfileResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockProfileCall, new RuntimeException("Sin conexión"));
            return null;
        }).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer token", profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("Sin conexión"));
        verify(loadingLiveData).postValue(false);
    }

    // ─── updateProfile ────────────────────────────────────────────────────────

    @Test
    public void updateProfile_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockUpdateCall).enqueue(any());

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void updateProfile_onSuccess_postsUpdatedProfile() {
        InfluencerProfileResponse updated = new InfluencerProfileResponse();
        updated.setInstagram("@laurastyle");
        updated.setTiktok("@lauratiktok");

        doAnswer(invocation -> {
            Callback<InfluencerProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.success(updated));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        verify(profileLiveData).postValue(updated);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void updateProfile_on409Response_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<InfluencerProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.error(409, mockErrorBody));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("409"));
        verify(loadingLiveData).postValue(false);
        verify(profileLiveData, never()).postValue(any());
    }

    @Test
    public void updateProfile_on401Response_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<InfluencerProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void updateProfile_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<InfluencerProfileResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockUpdateCall, new RuntimeException("Sin conexión"));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("Sin conexión"));
        verify(loadingLiveData).postValue(false);
        verify(profileLiveData, never()).postValue(any());
    }
}
