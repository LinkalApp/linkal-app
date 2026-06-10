package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.responses.MatchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MatchApiService {
    // RUTA DEL CONTROLADOR MATCH
    String base = "matches";

    @POST(base + "/campaigns/{campaignId}")
    Call<MatchResponse> createByInfluencer(@Header("Authorization") String token,
                                           @Path("campaignId") String campaignId);

    @POST(base + "/influencers/{influencerId}/campaigns/{campaignId}")
    Call<MatchResponse> createByBusiness(@Header("Authorization") String token,
                                         @Path("influencerId") String influencerId,
                                         @Path("campaignId") String campaignId);

    @GET(base + "/campaigns/{campaignId}/influencer")
    Call<MatchResponse> findByInfluencer(@Header("Authorization") String token,
                                         @Path("campaignId") String campaignId);
}
