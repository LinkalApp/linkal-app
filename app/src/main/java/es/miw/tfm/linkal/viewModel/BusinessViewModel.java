package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.miw.tfm.linkal.data.repositories.BusinessRepository;
import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import es.miw.tfm.linkal.models.requests.UpdateBusinessRequest;
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;
import es.miw.tfm.linkal.models.responses.BusinessProfileResponse;

public class BusinessViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<BusinessProfileResponse> profile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();

    private final BusinessRepository businessRepository;

    public BusinessViewModel() {
        this.businessRepository = BusinessRepository.getInstance();
    }

    /** Constructor package-private para inyección en tests. */
    BusinessViewModel(BusinessRepository repository) {
        this.businessRepository = repository;
    }

    // REGISTRO
    public void register(RegisterBusinessRequest request) {
        businessRepository.register(request, registerSuccess, errorMessage, isLoading);
    }

    // PERFIL
    public void loadProfile(String token) {
        businessRepository.getProfile(token, profile, errorMessage, isLoading);
    }

    public void updateProfile(String token, UpdateBusinessRequest request) {
        businessRepository.updateProfile(token, request, profile, errorMessage, isLoading);
        updateSuccess.setValue(true);
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }
    public LiveData<String>  getErrorMessage() { return errorMessage; }
    public LiveData<BusinessProfileResponse> getProfile() { return profile; }
    public LiveData<Boolean> getUpdateSuccess() { return updateSuccess; }
}