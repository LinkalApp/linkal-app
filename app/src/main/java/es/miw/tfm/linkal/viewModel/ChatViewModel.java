package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.miw.tfm.linkal.data.repositories.ChatRepository;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {

    private final ChatRepository repository;

    private final MutableLiveData<List<ChatResponse>> chats = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public ChatViewModel() {
        this.repository = new ChatRepository();
    }

    ChatViewModel(ChatRepository repository) {
        this.repository = repository;
    }

    public void loadChats(String token) {
        repository.findAllByUser(token).enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chats.postValue(response.body());
                } else {
                    error.postValue("Error al cargar chats: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {
                error.postValue("Error de red: " + t.getMessage());
            }
        });
    }

    public LiveData<List<ChatResponse>> getChats() { return chats; }
    public LiveData<String> getError() { return error; }

}
