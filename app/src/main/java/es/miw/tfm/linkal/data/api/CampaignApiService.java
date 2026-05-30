package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.CreateCampaignRequest;
import es.miw.tfm.linkal.models.responses.CampaignResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CampaignApiService {
    // RUTA DEL CONTROLADOR CAMPAIGN
    String base = "campaigns";

    @POST(base)
    Call<CampaignResponse> create(@Header("Authorization") String token,
                                  @Body CreateCampaignRequest request);
}
