package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.miw.tfm.linkal.data.repositories.BusinessRepository;
import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;

public class BusinessViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final BusinessRepository businessRepository = BusinessRepository.getInstance();

    // REGISTRO
    public void register(RegisterBusinessRequest request) {
        businessRepository.register(request, registerSuccess, errorMessage, isLoading);
    }

    public LiveData<Boolean> getIsLoading()       { return isLoading; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }
    public LiveData<String>  getErrorMessage()    { return errorMessage; }
}