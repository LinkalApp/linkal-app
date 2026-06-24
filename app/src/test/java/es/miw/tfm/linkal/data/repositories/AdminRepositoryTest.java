package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import es.miw.tfm.linkal.data.api.AdminApiService;
import es.miw.tfm.linkal.models.responses.AdminUserResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AdminRepositoryTest {

    @Mock private AdminApiService mockApiService;
    @Mock private Call<List<AdminUserResponse>> mockFindAllCall;
    @Mock private Call<AdminUserResponse> mockFindByIdCall;
    @Mock private MutableLiveData<List<AdminUserResponse>> usersLiveData;
    @Mock private MutableLiveData<AdminUserResponse> detailLiveData;
    @Mock private MutableLiveData<String> errorLiveData;
    @Mock private MutableLiveData<Boolean> loadingLiveData;
    @Mock private ResponseBody mockErrorBody;

    private AdminRepository repository;

    @Before
    public void setUp() {
        repository = new AdminRepository(mockApiService);
        when(mockApiService.findAll(anyString(), any(), any())).thenReturn(mockFindAllCall);
        when(mockApiService.findById(anyString(), anyString())).thenReturn(mockFindByIdCall);
    }

    // Singleton --------------------------------------------------------------------

    @Test
    public void getInstance_returnsSameInstance() {
        AdminRepository i1 = AdminRepository.getInstance();
        AdminRepository i2 = AdminRepository.getInstance();
        assertSame(i1, i2);
    }

    // findAll -------------------------------------------------------------------------

    @Test
    public void findAll_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockFindAllCall).enqueue(any());

        repository.findAll("Bearer token", null, null, usersLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void findAll_callsApiServiceWithCorrectParams() {
        doAnswer(inv -> null).when(mockFindAllCall).enqueue(any());

        repository.findAll("Bearer token", "INFLUENCER", true, usersLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).findAll(eq("Bearer token"), eq("INFLUENCER"), eq(true));
    }

    @Test
    public void findAll_callsEnqueueOnCall() {
        doAnswer(inv -> null).when(mockFindAllCall).enqueue(any());

        repository.findAll("Bearer token", null, null, usersLiveData, errorLiveData, loadingLiveData);

        verify(mockFindAllCall).enqueue(any());
    }

    @Test
    public void findAll_onSuccess_postsResultAndStopsLoading() {
        List<AdminUserResponse> users = Arrays.asList(buildUser("INFLUENCER"), buildUser("BUSINESS"));

        doAnswer(invocation -> {
            Callback<List<AdminUserResponse>> callback = invocation.getArgument(0);
            callback.onResponse(mockFindAllCall, Response.success(users));
            return null;
        }).when(mockFindAllCall).enqueue(any());

        repository.findAll("Bearer token", null, null, usersLiveData, errorLiveData, loadingLiveData);

        verify(usersLiveData).postValue(users);
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void findAll_onHttpError_postsErrorAndStopsLoading() throws Exception {
        when(mockErrorBody.string()).thenReturn("{\"message\": \"Forbidden\"}");

        doAnswer(invocation -> {
            Callback<List<AdminUserResponse>> callback = invocation.getArgument(0);
            callback.onResponse(mockFindAllCall, Response.error(403, mockErrorBody));
            return null;
        }).when(mockFindAllCall).enqueue(any());

        repository.findAll("Bearer token", null, null, usersLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("403"));
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void findAll_onNetworkFailure_postsErrorAndStopsLoading() {
        doAnswer(invocation -> {
            Callback<List<AdminUserResponse>> callback = invocation.getArgument(0);
            callback.onFailure(mockFindAllCall, new RuntimeException("Timeout"));
            return null;
        }).when(mockFindAllCall).enqueue(any());

        repository.findAll("Bearer token", null, null, usersLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("Timeout"));
        verify(loadingLiveData).postValue(false);
    }

    // findById ---------------------------------------------------------------------------------

    @Test
    public void findById_setsLoadingTrueOnStart() {
        doAnswer(inv -> null).when(mockFindByIdCall).enqueue(any());

        repository.findById("Bearer token", "user-id-123", detailLiveData, errorLiveData, loadingLiveData);

        verify(loadingLiveData).setValue(true);
    }

    @Test
    public void findById_callsApiServiceWithCorrectParams() {
        doAnswer(inv -> null).when(mockFindByIdCall).enqueue(any());

        repository.findById("Bearer token", "user-id-123", detailLiveData, errorLiveData, loadingLiveData);

        verify(mockApiService).findById(eq("Bearer token"), eq("user-id-123"));
    }

    @Test
    public void findById_onSuccess_postsDetailAndStopsLoading() {
        AdminUserResponse user = buildUser("INFLUENCER");

        doAnswer(invocation -> {
            Callback<AdminUserResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockFindByIdCall, Response.success(user));
            return null;
        }).when(mockFindByIdCall).enqueue(any());

        repository.findById("Bearer token", "user-id", detailLiveData, errorLiveData, loadingLiveData);

        verify(detailLiveData).postValue(user);
        verify(loadingLiveData).postValue(false);
    }

    @Test
    public void findById_onNotFound_postsErrorAndStopsLoading() throws Exception {
        when(mockErrorBody.string()).thenReturn("{\"message\": \"Not found\"}");

        doAnswer(invocation -> {
            Callback<AdminUserResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockFindByIdCall, Response.error(404, mockErrorBody));
            return null;
        }).when(mockFindByIdCall).enqueue(any());

        repository.findById("Bearer token", "bad-id", detailLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("404"));
    }

    @Test
    public void findById_onNetworkFailure_postsError() {
        doAnswer(invocation -> {
            Callback<AdminUserResponse> callback = invocation.getArgument(0);
            callback.onFailure(mockFindByIdCall, new RuntimeException("Connection refused"));
            return null;
        }).when(mockFindByIdCall).enqueue(any());

        repository.findById("Bearer token", "user-id", detailLiveData, errorLiveData, loadingLiveData);

        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(errorLiveData).postValue(errorCaptor.capture());
        assertTrue(errorCaptor.getValue().contains("Connection refused"));
    }

    // helpers -----------------------------------------------------------------------------------------------

    private AdminUserResponse buildUser(String role) {
        AdminUserResponse u = new AdminUserResponse();
        u.setId("user-id-" + role);
        u.setName("Test " + role);
        u.setEmail("test@" + role.toLowerCase() + ".com");
        u.setRole(role);
        u.setVerified(true);
        return u;
    }
}