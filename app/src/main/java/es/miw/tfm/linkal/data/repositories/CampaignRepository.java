package es.miw.tfm.linkal.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

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

    public void getOpenCampaigns(String token,
                                 MutableLiveData<List<CampaignResponse>> result,
                                 MutableLiveData<String> error,
                                 MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getOpenCampaigns(token).enqueue(
                new ApiCallback<List<CampaignResponse>>(loading, error) {
                    @Override
                    protected void onSuccess(List<CampaignResponse> body) {
                        result.postValue(body);
                    }
                });
    }

    public void getOpenCampaignsByFilters(String token,
                                          String category,
                                          String province,
                                          MutableLiveData<List<CampaignResponse>> result,
                                          MutableLiveData<String> error,
                                          MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getOpenCampaignsByFilters(token, category, province).enqueue(
                new ApiCallback<List<CampaignResponse>>(loading, error) {
                    @Override
                    protected void onSuccess(List<CampaignResponse> body) {
                        result.postValue(body);
                    }
                });
    }

    public void startWithInfluencer(String token,
                                    String campaignId,
                                    String matchId,
                                    MutableLiveData<CampaignResponse> result,
                                    MutableLiveData<String> error,
                                    MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.startWithInfluencer(token, campaignId, matchId).enqueue(
                new ApiCallback<CampaignResponse>(loading, error) {
                    @Override
                    protected void onSuccess(CampaignResponse body) {
                        result.postValue(body);
                    }
                });
    }
}
