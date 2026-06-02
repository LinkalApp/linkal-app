package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.miw.tfm.linkal.data.repositories.InfluencerRepository;
import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;

public class InfluencerViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<InfluencerProfileResponse> profile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
    private final MutableLiveData<List<InfluencerProfileResponse>> influencers = new MutableLiveData<>();

    private final InfluencerRepository influencerRepository;

    public InfluencerViewModel() {
        this.influencerRepository = InfluencerRepository.getInstance();
    }

    /** Constructor package-private para inyección en tests. */
    InfluencerViewModel(InfluencerRepository influencerRepository) {
        this.influencerRepository = influencerRepository;
    }

    // REGISTRO
    public void register(RegisterInfluencerRequest request) {
        influencerRepository.register(request, registerSuccess, errorMessage, isLoading);
    }

    // PERFIL
    public void loadProfile(String token) {
        influencerRepository.getProfile(token, profile, errorMessage, isLoading);
    }

    public void updateProfile(String token, UpdateInfluencerRequest request) {
        influencerRepository.updateProfile(token, request, profile, errorMessage, isLoading);
        updateSuccess.setValue(true);
    }

    // ELIMINAR CUENTA
    public void deleteAccount(String token) {
        influencerRepository.deleteAccount(token, deleteSuccess, errorMessage, isLoading);
    }

    // EXPLORAR INFLUENCERS (uso business)
    public void loadAll(String token) {
        influencerRepository.getAll(token, influencers, errorMessage, isLoading);
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getRegisterSuccess() { return registerSuccess; }
    public LiveData<String>  getErrorMessage() { return errorMessage; }
    public LiveData<InfluencerProfileResponse> getProfile() { return profile; }
    public LiveData<Boolean> getUpdateSuccess() { return updateSuccess; }
    public LiveData<Boolean> getDeleteSuccess() { return deleteSuccess; }
    public LiveData<List<InfluencerProfileResponse>> getInfluencers() { return influencers; }
}
