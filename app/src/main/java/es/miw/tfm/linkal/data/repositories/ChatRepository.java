package es.miw.tfm.linkal.data.repositories;

import java.util.List;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.ChatApiService;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import retrofit2.Call;

public class ChatRepository {
    private final ChatApiService api;

    public ChatRepository() {
        this.api = ApiClient.getChatApiService();
    }

    ChatRepository(ChatApiService api) {
        this.api = api;
    }

    public Call<List<ChatResponse>> findAllByUser(String token) {
        return api.findAllByUser(token);
    }
}
