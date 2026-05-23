package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.miw.tfm.linkal.data.repositories.AuthRepository;
import es.miw.tfm.linkal.models.responses.AuthResponse;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<AuthResponse> authResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean>      success    = new MutableLiveData<>();
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

    public LiveData<AuthResponse> getAuthResult() { return authResult; }
    public LiveData<Boolean>      getSuccess()    { return success; }
    public LiveData<String>       getError()      { return error; }
    public LiveData<Boolean> getLoading()    { return loading; }

    public void login(String email, String password) {
        authRepository.login(email, password, authResult, error, loading);
    }

    public void forgotPassword(String email) {
        authRepository.forgotPassword(email, success, error, loading);
    }

    public void resetPassword(String email, String code, String newPassword) {
        authRepository.resetPassword(email, code, newPassword, success, error, loading);
    }
}
