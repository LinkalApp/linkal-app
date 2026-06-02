package es.miw.tfm.linkal.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.CampaignApiService;
import es.miw.tfm.linkal.models.requests.CreateCampaignRequest;
import es.miw.tfm.linkal.models.requests.UpdateCampaignRequest;
import es.miw.tfm.linkal.models.responses.CampaignResponse;

public class CampaignRepository extends BaseRepository{
    private static CampaignRepository instance;
    private final CampaignApiService apiService;

    private CampaignRepository() {
        this.apiService = ApiClient.getCampaignApiService();
    }

    /** Constructor package-private para inyección en tests. */
    CampaignRepository(CampaignApiService apiService) {
        this.apiService = apiService;
    }

    public static CampaignRepository getInstance() {
        if (instance == null) {
            instance = new CampaignRepository();
        }
        return instance;
    }

    public void create(String token,
                       CreateCampaignRequest request,
                       MutableLiveData<CampaignResponse> result,
                       MutableLiveData<String> error,
                       MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.create(token, request).enqueue(
                new ApiCallback<CampaignResponse>(loading, error) {
                    @Override
                    protected void onSuccess(CampaignResponse body) {
                        result.postValue(body);
                    }
                });
    }

    public void update(String token,
                       String campaignId,
                       UpdateCampaignRequest request,
                       MutableLiveData<CampaignResponse> result,
                       MutableLiveData<String> error,
                       MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.update(token, campaignId, request).enqueue(
                new ApiCallback<CampaignResponse>(loading, error) {
                    @Override
                    protected void onSuccess(CampaignResponse body) {
                        result.postValue(body);
                    }
                });
    }

    public void delete(String token,
                       String campaignId,
                       MutableLiveData<Boolean> result,
                       MutableLiveData<String> error,
                       MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.delete(token, campaignId).enqueue(
                new ApiCallback<Void>(loading, error) {
                    @Override
                    protected void onSuccess(Void body) {
                        result.postValue(true);
                    }
                });
    }
}
