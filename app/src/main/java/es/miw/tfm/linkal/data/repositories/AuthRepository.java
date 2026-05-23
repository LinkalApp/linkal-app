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

public class AuthRepository {
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
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    authResult.postValue(response.body());
                } else {
                    error.postValue("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                loading.postValue(false);
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Forgot password ------------------------------------------------------------------

    public void forgotPassword(String email,
                               MutableLiveData<Boolean> success,
                               MutableLiveData<String> error,
                               MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.forgotPassword(new ForgotPasswordRequest(email)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loading.postValue(false);
                if (response.isSuccessful()) {
                    success.postValue(true);
                } else {
                    error.postValue("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.postValue(false);
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Reset password ------------------------------------------------------------------

    public void resetPassword(String email, String code, String newPassword,
                              MutableLiveData<Boolean> success,
                              MutableLiveData<String> error,
                              MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.resetPassword(new ResetPasswordRequest(email, code, newPassword)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loading.postValue(false);
                if (response.isSuccessful()) {
                    success.postValue(true);
                } else {
                    error.postValue("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.postValue(false);
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
