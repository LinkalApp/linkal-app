package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.miw.tfm.linkal.data.repositories.BusinessRepository;
import es.miw.tfm.linkal.data.repositories.CampaignRepository;
import es.miw.tfm.linkal.models.requests.CreateCampaignRequest;
import es.miw.tfm.linkal.models.requests.UpdateCampaignRequest;
import es.miw.tfm.linkal.models.responses.CampaignResponse;

public class CampaignViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<CampaignResponse> createResult = new MutableLiveData<>();
    private final MutableLiveData<CampaignResponse> updateResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();
    private final MutableLiveData<List<CampaignResponse>> campaigns = new MutableLiveData<>();
    private final MutableLiveData<List<CampaignResponse>> openCampaigns = new MutableLiveData<>();
    private final MutableLiveData<CampaignResponse> startResult = new MutableLiveData<>();

    private final CampaignRepository campaignRepository;
    private final BusinessRepository businessRepository;

    public CampaignViewModel() {
        this.campaignRepository = CampaignRepository.getInstance();
        this.businessRepository = BusinessRepository.getInstance();
    }


    /** Constructor package-private para inyección en tests. */
    CampaignViewModel(CampaignRepository campaignRepository,
                      BusinessRepository businessRepository) {
        this.campaignRepository = campaignRepository;
        this.businessRepository = businessRepository;
    }
    public void create(String token, CreateCampaignRequest request) {
        campaignRepository.create(token, request, createResult, errorMessage, isLoading);
    }

    public void loadByBusiness(String token, String businessId) {
        businessRepository.getCampaigns(token, businessId, campaigns, errorMessage, isLoading);
    }

    public void loadOpenCampaigns(String token) {
        campaignRepository.getOpenCampaigns(token, openCampaigns, errorMessage, isLoading);
    }

    public void loadOpenCampaignsByFilters(String token, String category, String province) {
        boolean hasFilter = (category != null && !category.isEmpty())
                || (province != null && !province.isEmpty());
        if (hasFilter) {
            campaignRepository.getOpenCampaignsByFilters(
                    token, category, province, openCampaigns, errorMessage, isLoading);
        } else {
            loadOpenCampaigns(token);
        }
    }

    public void update(String token, String campaignId, UpdateCampaignRequest request) {
        campaignRepository.update(token, campaignId, request, updateResult, errorMessage, isLoading);
    }

    public void delete(String token, String campaignId) {
        campaignRepository.delete(token, campaignId, deleteResult, errorMessage, isLoading);
    }

    public void startWithInfluencer(String token, String campaignId, String matchId) {
        campaignRepository.startWithInfluencer(token, campaignId, matchId, startResult, errorMessage, isLoading);
    }

    public LiveData<Boolean> getIsLoading()    { return isLoading; }
    public LiveData<String> getError() { return errorMessage; }
    public LiveData<CampaignResponse> getCreateResult() { return createResult; }
    public LiveData<CampaignResponse> getUpdateResult() { return updateResult; }
    public LiveData<Boolean> getDeleteResult() { return deleteResult; }
    public LiveData<List<CampaignResponse>> getCampaigns() { return campaigns; }
    public LiveData<List<CampaignResponse>> getOpenCampaigns() { return openCampaigns; }
    public LiveData<CampaignResponse> getStartResult() { return startResult; }
}
