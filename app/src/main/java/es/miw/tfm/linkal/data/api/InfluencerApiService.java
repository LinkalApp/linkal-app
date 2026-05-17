package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InfluencerApiService {
    // RUTA DEL CONTROLADOR INFLUENCER
    String base = "influencers";
    @POST(base)
    Call<Void> register(@Body RegisterInfluencerRequest request);
}
