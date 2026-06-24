package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.EvaluationRequest;
import es.miw.tfm.linkal.models.responses.EvaluationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EvaluationApiService {
    // RUTA DEL CONTROLADOR EVALUATION
    String base = "evaluations";
    @POST(base + "/matches/{matchId}")
    Call<EvaluationResponse> create(@Header("Authorization") String token,
                                    @Path("matchId") String matchId,
                                    @Body EvaluationRequest request);
}
