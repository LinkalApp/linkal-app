package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.miw.tfm.linkal.data.repositories.CampaignRepository;
import es.miw.tfm.linkal.models.requests.CreateCampaignRequest;
import es.miw.tfm.linkal.models.responses.CampaignResponse;

public class CampaignViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<CampaignResponse> createResult = new MutableLiveData<>();

    private final CampaignRepository campaignRepository;

    public CampaignViewModel() {
        this.campaignRepository = CampaignRepository.getInstance();
    }

    /** Constructor package-private para inyección en tests. */
    CampaignViewModel(CampaignRepository repository) {
        this.campaignRepository = repository;
    }
    public void create(String token, CreateCampaignRequest request) {
        campaignRepository.create(token, request, createResult, errorMessage, isLoading);
    }

    public LiveData<Boolean> getIsLoading()    { return isLoading; }
    public LiveData<String> getError() { return errorMessage; }
    public LiveData<CampaignResponse> getCreateResult() { return createResult; }
}
