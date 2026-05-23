package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.ForgotPasswordRequest;
import es.miw.tfm.linkal.models.requests.LoginRequest;
import es.miw.tfm.linkal.models.requests.ResetPasswordRequest;
import es.miw.tfm.linkal.models.responses.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    String base = "auth";
    @POST(base + "/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST( base + "/forgot-password")
    Call<Void> forgotPassword(@Body ForgotPasswordRequest request);

    @POST(base + "/reset-password")
    Call<Void> resetPassword(@Body ResetPasswordRequest request);
}
