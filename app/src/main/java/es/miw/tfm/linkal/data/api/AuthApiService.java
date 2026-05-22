package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.LoginRequest;
import es.miw.tfm.linkal.models.responses.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
}
