package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import es.miw.tfm.linkal.models.responses.BusinessProfileResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BusinessApiService {
    // RUTA DEL CONTROLADOR BUSINESS
    String base = "businesses";
    @POST(base)
    Call<Void> register(@Body RegisterBusinessRequest request);

    @GET(base + "/me")
    Call<BusinessProfileResponse> getProfile(@Header("Authorization") String token);
}
