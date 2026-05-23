package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.api.AuthApiService;
import es.miw.tfm.linkal.models.responses.AuthResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AuthRepositoryTest {

    @Mock
    private AuthApiService mockApiService;
    @Mock
    private Call<AuthResponse> mockLoginCall;
    @Mock
    private Call<Void> mockVoidCall;
    @Mock
    private ResponseBody mockErrorBody;
    @Mock
    private MutableLiveData<AuthResponse> authResultLiveData;
    @Mock
    private MutableLiveData<Boolean> successLiveData;
    @Mock
    private MutableLiveData<String> errorLiveData;
    @Mock
    private MutableLiveData<Boolean> loadingLiveData;

    private AuthRepository repository;

    @Before
    public void setUp() {
        repository = new AuthRepository(mockApiService);
        when(mockApiService.login(any())).thenReturn(mockLoginCall);
        when(mockApiService.forgotPassword(any())).thenReturn(mockVoidCall);
        when(mockApiService.resetPassword(any())).thenReturn(mockVoidCall);
    }

    // Singleton -----------------------------------------------------------------

    @Test
    public void getInstance_returnsSameInstance() {
        AuthRepository i1 = AuthRepository.getInstance();
        AuthRepository i2 = AuthRepository.getInstance();
        assertSame(i1, i2);
    }

    // Estado inicial de loading ------------------------------------------------

    @Test
    public void login_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockLoginCall).enqueue(any());

        repository.login("user@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    // Respuesta exitosa 200 ------------------------------------------------------

    @Test
    public void login_onSuccessfulResponse_postsAuthResult() {
        AuthResponse fakeResponse = new AuthResponse("token-abc", "BUSINESS", "user@test.com");

        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockLoginCall, Response.success(fakeResponse));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("user@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        verify(authResultLiveData).postValue(fakeResponse);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    @Test
    public void login_onSuccessForInfluencer_postsAuthResultWithInfluencerRole() {
        AuthResponse fakeResponse = new AuthResponse("token-xyz", "INFLUENCER", "inf@test.com");

        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockLoginCall, Response.success(fakeResponse));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("inf@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<AuthResponse> captor = ArgumentCaptor.forClass(AuthResponse.class);
        verify(authResultLiveData).postValue(captor.capture());
        assertEquals("INFLUENCER", captor.getValue().getRole());
        verify(loadingLiveData).postValue(false);
    }

    // Respuesta exitosa con body null --------------------------------------------------------

    @Test
    public void login_onSuccessWithNullBody_postsError() {
        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockLoginCall, Response.success(null));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("user@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
        verify(authResultLiveData, never()).postValue(any());
    }

    // Respuesta con error HTTP 401 -----------------------------------------------------------

    @Test
    public void login_onError401_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockLoginCall, Response.error(401, mockErrorBody));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("user@test.com", "wrongPass", authResultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el código 401",
                captor.getValue().contains("401"));
        verify(loadingLiveData).postValue(false);
        verify(authResultLiveData, never()).postValue(any());
    }

    // Respuesta con error HTTP 404 -----------------------------------------------------------

    @Test
    public void login_onError404_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockLoginCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("unknown@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el código 404",
                captor.getValue().contains("404"));
        verify(loadingLiveData).postValue(false);
    }

    // Respuesta con error HTTP 500 -------------------------------------------------------------

    @Test
    public void login_on500Response_postsErrorMessageWithCode() {
        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onResponse(mockLoginCall, Response.error(500, mockErrorBody));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("user@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el código 500",
                captor.getValue().contains("500"));
        verify(loadingLiveData).postValue(false);
    }

    // Fallo de red ------------------------------------------------------------------------------

    @Test
    public void login_onNetworkFailure_postsConnectionErrorMessage() {
        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockLoginCall, new RuntimeException("timeout"));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("user@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue("El mensaje debe contener el texto del error",
                captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(authResultLiveData, never()).postValue(any());
    }

    @Test
    public void login_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<AuthResponse> cb = invocation.getArgument(0);
            cb.onFailure(mockLoginCall, new RuntimeException((String) null));
            return null;
        }).when(mockLoginCall).enqueue(any());

        repository.login("user@test.com", "pass", authResultLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // --------------------------------------------------------------------------
    //  forgotPassword
    // --------------------------------------------------------------------------

    // Estado inicial de loading ------------------------------------------------

    @Test
    public void forgotPassword_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockVoidCall).enqueue(any());

        repository.forgotPassword("user@test.com", successLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    // Respuesta exitosa 200 -------------------------------------------------------

    @Test
    public void forgotPassword_onSuccess_postsSuccessTrue() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockVoidCall, Response.success(null));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.forgotPassword("user@test.com", successLiveData, errorLiveData, loadingLiveData);

        verify(successLiveData).postValue(true);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    // Respuesta con error HTTP --------------------------------------------------------------

    @Test
    public void forgotPassword_onError404_postsErrorWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockVoidCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.forgotPassword("unknown@test.com", successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("404"));
        verify(loadingLiveData).postValue(false);
        verify(successLiveData, never()).postValue(any());
    }

    @Test
    public void forgotPassword_onError500_postsErrorWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockVoidCall, Response.error(500, mockErrorBody));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.forgotPassword("user@test.com", successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("500"));
        verify(loadingLiveData).postValue(false);
    }

    // Fallo de red ------------------------------------------------------------------------

    @Test
    public void forgotPassword_onNetworkFailure_postsConnectionError() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockVoidCall, new RuntimeException("timeout"));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.forgotPassword("user@test.com", successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("timeout"));
        verify(loadingLiveData).postValue(false);
        verify(successLiveData, never()).postValue(any());
    }

    @Test
    public void forgotPassword_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockVoidCall, new RuntimeException((String) null));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.forgotPassword("user@test.com", successLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }

    // --------------------------------------------------------------------------
    //  resetPassword
    // --------------------------------------------------------------------------

    // Estado inicial de loading ------------------------------------------------

    @Test
    public void resetPassword_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockVoidCall).enqueue(any());

        repository.resetPassword("user@test.com", "123456", "newPass1",
                successLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    // Respuesta exitosa 200 ----------------------------------------------------

    @Test
    public void resetPassword_onSuccess_postsSuccessTrue() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockVoidCall, Response.success(null));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.resetPassword("user@test.com", "123456", "newPass1",
                successLiveData, errorLiveData, loadingLiveData);

        verify(successLiveData).postValue(true);
        verify(loadingLiveData).postValue(false);
        verify(errorLiveData, never()).postValue(any());
    }

    // Respuesta con error HTTP -------------------------------------------------------

    @Test
    public void resetPassword_onError400_postsErrorWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockVoidCall, Response.error(400, mockErrorBody));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.resetPassword("user@test.com", "000000", "newPass1",
                successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("400"));
        verify(loadingLiveData).postValue(false);
        verify(successLiveData, never()).postValue(any());
    }

    @Test
    public void resetPassword_onError404_postsErrorWithCode() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onResponse(mockVoidCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.resetPassword("unknown@test.com", "123456", "newPass1",
                successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("404"));
        verify(loadingLiveData).postValue(false);
    }

    // Fallo de red ----------------------------------------------------------------

    @Test
    public void resetPassword_onNetworkFailure_postsConnectionError() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockVoidCall, new RuntimeException("connection refused"));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.resetPassword("user@test.com", "123456", "newPass1",
                successLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(captor.capture());
        assertTrue(captor.getValue().contains("connection refused"));
        verify(loadingLiveData).postValue(false);
        verify(successLiveData, never()).postValue(any());
    }

    @Test
    public void resetPassword_onNullThrowableMessage_postsErrorMessage() {
        doAnswer(invocation -> {
            Callback<Void> cb = invocation.getArgument(0);
            cb.onFailure(mockVoidCall, new RuntimeException((String) null));
            return null;
        }).when(mockVoidCall).enqueue(any());

        repository.resetPassword("user@test.com", "123456", "newPass1",
                successLiveData, errorLiveData, loadingLiveData);

        verify(errorLiveData).postValue(any());
        verify(loadingLiveData).postValue(false);
    }
}
