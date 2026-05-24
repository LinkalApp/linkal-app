package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface InfluencerApiService {
    // RUTA DEL CONTROLADOR INFLUENCER
    String base = "influencers";
    @POST(base)
    Call<Void> register(@Body RegisterInfluencerRequest request);

    @GET(base + "/me")
    Call<InfluencerProfileResponse> getProfile(@Header("Authorization") String token);

    @PUT(base + "/me")
    Call<InfluencerProfileResponse> updateProfile(@Header("Authorization") String token,
                                                  @Body UpdateInfluencerRequest request);

    @DELETE(base + "/me")
    Call<Void> deleteAccount(@Header("Authorization") String token);
}
