package es.miw.tfm.linkal.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.AuthApiService;
import es.miw.tfm.linkal.models.requests.ForgotPasswordRequest;
import es.miw.tfm.linkal.models.requests.LoginRequest;
import es.miw.tfm.linkal.models.requests.ResetPasswordRequest;
import es.miw.tfm.linkal.models.responses.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository extends BaseRepository {
    private static AuthRepository instance;
    private final AuthApiService apiService;

    private AuthRepository() {
        this.apiService = ApiClient.getAuthApiService();
    }

    /** Constructor package-private para inyección en tests. */
    AuthRepository(AuthApiService apiService) {
        this.apiService = apiService;
    }

    public static AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    // Login ------------------------------------------------------------------

    public void login(String email, String password,
                      MutableLiveData<AuthResponse> authResult,
                      MutableLiveData<String> error,
                      MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.login(new LoginRequest(email, password)).enqueue(
                new ApiCallback<AuthResponse>(loading, error) {
                    @Override
                    protected void onSuccess(AuthResponse body) {
                        if (body != null) {
                            authResult.postValue(body);
                        } else {
                            postError("Error: respuesta vacía del servidor");
                        }
                    }
                });
    }

    // Forgot password ------------------------------------------------------------------

    public void forgotPassword(String email,
                               MutableLiveData<Boolean> success,
                               MutableLiveData<String> error,
                               MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.forgotPassword(new ForgotPasswordRequest(email)).enqueue(
                new ApiCallback<Void>(loading, error) {
                    @Override
                    protected void onSuccess(Void body) {
                        success.postValue(true);
                    }
                });
    }

    // Reset password ------------------------------------------------------------------

    public void resetPassword(String email, String code, String newPassword,
                              MutableLiveData<Boolean> success,
                              MutableLiveData<String> error,
                              MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.resetPassword(new ResetPasswordRequest(email, code, newPassword)).enqueue(
                new ApiCallback<Void>(loading, error) {
                    @Override
                    protected void onSuccess(Void body) {
                        success.postValue(true);
                    }
                });
    }
}
