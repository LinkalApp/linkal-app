package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import es.miw.tfm.linkal.models.requests.UpdateBusinessRequest;
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;
import es.miw.tfm.linkal.models.responses.BusinessProfileResponse;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface BusinessApiService {
    // RUTA DEL CONTROLADOR BUSINESS
    String base = "businesses";
    @POST(base)
    Call<Void> register(@Body RegisterBusinessRequest request);

    @GET(base + "/me")
    Call<BusinessProfileResponse> getProfile(@Header("Authorization") String token);

    @PUT(base + "/me")
    Call<BusinessProfileResponse> updateProfile(@Header("Authorization") String token,
                                                  @Body UpdateBusinessRequest request);

    @DELETE(base + "/me")
    Call<Void> deleteAccount(@Header("Authorization") String token);
}
