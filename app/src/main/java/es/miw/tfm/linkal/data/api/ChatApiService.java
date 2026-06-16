package es.miw.tfm.linkal.data.api;

import java.util.List;

import es.miw.tfm.linkal.models.responses.ChatResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ChatApiService {
    // RUTA DEL CONTROLADOR CHAT
    String base = "chats";
    @GET(base)
    Call<List<ChatResponse>> findAllByUser(@Header("Authorization") String token);
}
