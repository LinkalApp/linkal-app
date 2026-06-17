package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.ChatApiService;
import es.miw.tfm.linkal.models.requests.SendMessageRequest;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import es.miw.tfm.linkal.models.responses.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRepository extends BaseRepository{
    private static ChatRepository instance;
    private final ChatApiService api;

    public ChatRepository() {
        this.api = ApiClient.getChatApiService();
    }

    ChatRepository(ChatApiService api) {
        this.api = api;
    }

    public static ChatRepository getInstance() {
        if (instance == null) {
            instance = new ChatRepository();
        }
        return instance;
    }

    public void findAllByUser(String token,
                              MutableLiveData<List<ChatResponse>> chatsLiveData,
                              MutableLiveData<String> errorLiveData,
                              MutableLiveData<Boolean> loadingLiveData) {
        loadingLiveData.setValue(true);
        api.findAllByUser(token).enqueue(
                new ApiCallback<List<ChatResponse>>(loadingLiveData, errorLiveData) {
                    @Override
                    protected void onSuccess(List<ChatResponse> body) {
                        chatsLiveData.postValue(body);
                    }
                });
    }


    public void sendMessage(String token, String chatId, String text,
                            MutableLiveData<Boolean> messageSentLiveData,
                            MutableLiveData<String> errorLiveData,
                            MutableLiveData<Boolean> loadingLiveData) {
        loadingLiveData.setValue(true);
        SendMessageRequest body = new SendMessageRequest(text);
        api.sendMessage(token, chatId, body).enqueue(
                new ApiCallback<MessageResponse>(loadingLiveData, errorLiveData) {
                    @Override
                    protected void onSuccess(MessageResponse responseBody) {
                        messageSentLiveData.postValue(true);
                    }
                });
    }
}
