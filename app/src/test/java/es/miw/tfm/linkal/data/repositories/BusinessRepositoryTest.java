package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.api.BusinessApiService;
import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import es.miw.tfm.linkal.models.requests.UpdateBusinessRequest;
import es.miw.tfm.linkal.models.responses.BusinessProfileResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BusinessRepositoryTest {

    @Mock private BusinessApiService mockApiService;
    @Mock private Call<Void> mockCall;
    @Mock private Call<BusinessProfileResponse> mockProfileCall;
    @Mock private Call<BusinessProfileResponse> mockUpdateCall;
    @Mock private ResponseBody mockErrorBody;
    @Mock private MutableLiveData<Boolean> successLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;
    @Mock private MutableLiveData<BusinessProfileResponse>  profileLiveData;


    private BusinessRepository      repository;
    private RegisterBusinessRequest request;

    @Before
    public void setUp() {
        repository = new BusinessRepository(mockApiService);
        request = new RegisterBusinessRequest(
                "Empresa Test", "empresa@test.com", "SecurePass1.", "611000000",
                "Descripción", "Calle Mayor 1", "Madrid",
                "https://empresa.com", "Tecnología"
        );
        when(mockApiService.register(any())).thenReturn(mockCall);
        when(mockApiService.getProfile(anyString())).thenReturn(mockProfileCall);
        when(mockApiService.updateProfile(anyString(), any())).thenReturn(mockUpdateCall);
    }

    // Singleton ------------------------------------------------------------------

    @Test
    public void getInstance_returnsSameInstance() {
        BusinessRepository i1 = BusinessRepository.getInstance();
        BusinessRepository i2 = BusinessRepository.getInstance();
        assertSame(i1, i2);
    }

    // Estado inicial de loading --------------------------------------------------

    @Test
    public void register_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    // Respuesta exitosa ----------------------------------------------------------------

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

    // Respuesta con error HTTP ---------------------------------------------------------------

    @Test
    public void register_onErrorResponse_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el código 401",
                captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(successLiveData, never()).postValue(any());
    }

    @Test
    public void register_on500Response_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockCall, Response.error(500, mockErrorBody));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el código 500",
                captor.getValue().contains("500"));
        verify(loadingLiveData).postValue(false);
    }

    // Fallo de red ------------------------------------------------------------------------

    @Test
    public void register_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockCall, new RuntimeException("timeout"));
            return null;
        }).when(mockCall).enqueue(any());

        repository.register(request, successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el texto del error",
                captor.getValue().contains("timeout"));
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

    // getProfile -------------------------------------------------------------------------
    @Test
    public void getProfile_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer token", profileLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void getProfile_onSuccess_postsProfileData() {
        BusinessProfileResponse profile = new BusinessProfileResponse();
        profile.setEmail("empresa@test.com");
        profile.setAddress("Calle Mayor 1");

        doAnswer(invocation -> {
            Callback<BusinessProfileResponse> cb = invocation.getArgument(0);
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
            Callback<BusinessProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockProfileCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer expired", profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(profileLiveData, never()).postValue(any());
    }

    @Test
    public void getProfile_on403Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<BusinessProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockProfileCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer token", profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void getProfile_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<BusinessProfileResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockProfileCall, new RuntimeException("Sin conexión"));
            return null;
        }).when(mockProfileCall).enqueue(any());

        repository.getProfile("Bearer token", profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("Sin conexión"));
        verify(loadingLiveData).postValue(false);
        verify(profileLiveData, never()).postValue(any());
    }

    // updateProfile --------------------------------------------------------------------

    @Test
    public void updateProfile_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockUpdateCall).enqueue(any());

        UpdateBusinessRequest updateRequest = new UpdateBusinessRequest(
                "Empresa Test", "611000000", "Descripción",
                "Calle Mayor 1", "Madrid", "https://empresa.com");

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void updateProfile_onSuccess_postsUpdatedProfile() {
        BusinessProfileResponse updated = new BusinessProfileResponse();
        updated.setEmail("empresa@test.com");
        updated.setAddress("Calle Mayor 1");

        doAnswer(invocation -> {
            Callback<BusinessProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.success(updated));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        UpdateBusinessRequest updateRequest = new UpdateBusinessRequest(
                "Empresa Test", "611000000", "Descripción",
                "Calle Mayor 1", "Madrid", "https://empresa.com");

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        verify(profileLiveData).postValue(updated);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void updateProfile_on400Response_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<BusinessProfileResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockUpdateCall, Response.error(400, mockErrorBody));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        UpdateBusinessRequest updateRequest = new UpdateBusinessRequest(
                "Empresa Test", "611000000", "Descripción",
                "Calle Mayor 1", "Madrid", "https://empresa.com");

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("400"));
        verify(loadingLiveData).postValue(false);
        verify(profileLiveData, never()).postValue(any());
    }

    @Test
    public void updateProfile_onNetworkFailure_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<BusinessProfileResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockUpdateCall, new RuntimeException("timeout"));
            return null;
        }).when(mockUpdateCall).enqueue(any());

        UpdateBusinessRequest updateRequest = new UpdateBusinessRequest(
                "Empresa Test", "611000000", "Descripción",
                "Calle Mayor 1", "Madrid", "https://empresa.com");

        repository.updateProfile("Bearer token", updateRequest, profileLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(profileLiveData, never()).postValue(any());
    }
}