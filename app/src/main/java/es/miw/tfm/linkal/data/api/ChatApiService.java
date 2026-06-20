package es.miw.tfm.linkal.data.api;

import java.util.List;

import es.miw.tfm.linkal.models.requests.SendMessageRequest;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import es.miw.tfm.linkal.models.responses.MessageResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApiService {
    // RUTA DEL CONTROLADOR CHAT
    String base = "chats";
    @GET(base)
    Call<List<ChatResponse>> findAllByUser(@Header("Authorization") String token);

    @POST(base + "/{chatId}/messages")
    Call<MessageResponse> sendMessage(@Header("Authorization") String token,
                                      @Path("chatId") String chatId,
                                      @Body SendMessageRequest request);

    @GET(base + "/{chatId}/messages")
    Call<List<MessageResponse>> getMessages(@Header("Authorization") String token,
                                            @Path("chatId") String chatId);
}
