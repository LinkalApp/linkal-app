package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BusinessApiService {
    // RUTA DEL CONTROLADOR BUSINESS
    String base = "businesses";
    @POST(base)
    Call<Void> register(@Body RegisterBusinessRequest request);
}
