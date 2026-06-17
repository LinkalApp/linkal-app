package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.miw.tfm.linkal.data.repositories.ChatRepository;
import es.miw.tfm.linkal.models.responses.ChatResponse;
import es.miw.tfm.linkal.models.responses.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {

    private final ChatRepository repository;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<ChatResponse>> chats = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> messageSent = new MutableLiveData<>();

    public ChatViewModel() {
        this.repository = new ChatRepository();
    }

    ChatViewModel(ChatRepository repository) {
        this.repository = repository;
    }

    public void loadChats(String token) {
        repository.findAllByUser(token, chats, error, isLoading);
    }

    public void sendMessage(String token, String chatId, String text) {
        repository.sendMessage(token, chatId, text, messageSent, error, isLoading);
    }

    public LiveData<List<ChatResponse>> getChats() { return chats; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getMessageSent() { return messageSent; }
}
