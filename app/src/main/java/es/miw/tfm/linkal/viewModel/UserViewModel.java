package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.miw.tfm.linkal.data.repositories.AuthRepository;
import es.miw.tfm.linkal.models.responses.AuthResponse;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<AuthResponse> authResult = new MutableLiveData<>();
    private final MutableLiveData<String>       error      = new MutableLiveData<>();
    private final MutableLiveData<Boolean>      loading    = new MutableLiveData<>(false);

    private final AuthRepository authRepository;

    public UserViewModel() {
        this.authRepository = AuthRepository.getInstance();
    }

    /** Constructor package-private para inyección en tests. */
    UserViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public MutableLiveData<AuthResponse> getAuthResult() { return authResult; }
    public MutableLiveData<String>       getError()      { return error; }
    public MutableLiveData<Boolean>      getLoading()    { return loading; }

    public void login(String email, String password) {
        authRepository.login(email, password, authResult, error, loading);
    }
}
