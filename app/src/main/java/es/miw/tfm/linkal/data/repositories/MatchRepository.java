package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.MatchApiService;
import es.miw.tfm.linkal.models.responses.MatchResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchRepository extends BaseRepository{
    private static MatchRepository instance;
    private final MatchApiService apiService;

    private MatchRepository() {
        this.apiService = ApiClient.getMatchApiService();
    }

    MatchRepository(MatchApiService apiService) {
        this.apiService = apiService;
    }

    public static MatchRepository getInstance() {
        if (instance == null) {
            instance = new MatchRepository();
        }
        return instance;
    }

    public void createByInfluencer(String token,
                                   String campaignId,
                                   MutableLiveData<MatchResponse> result,
                                   MutableLiveData<String> error,
                                   MutableLiveData<Boolean> loading) {
        apiService.createByInfluencer(token, campaignId).enqueue(
                new ApiCallback<MatchResponse>(loading, error) {
                    @Override
                    protected void onSuccess(MatchResponse body) {
                        result.postValue(body);
                    }
                });
    }

    public void createByBusiness(String token,
                                 String influencerId,
                                 String campaignId,
                                 MutableLiveData<MatchResponse> result,
                                 MutableLiveData<String> error,
                                 MutableLiveData<Boolean> loading) {
        apiService.createByBusiness(token, influencerId, campaignId).enqueue(
                new ApiCallback<MatchResponse>(loading, error) {
                    @Override
                    protected void onSuccess(MatchResponse body) {
                        result.postValue(body);
                    }
                });
    }

    public void findByInfluencer(String token,
                                 String campaignId,
                                 MutableLiveData<MatchResponse> result,
                                 MutableLiveData<Boolean> notFound) {
        apiService.findByInfluencer(token, campaignId).enqueue(new Callback<MatchResponse>() {
            @Override
            public void onResponse(Call<MatchResponse> call, Response<MatchResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body());
                } else if (response.code() == 404) {
                    notFound.postValue(true);
                }
            }

            @Override
            public void onFailure(Call<MatchResponse> call, Throwable t) {
                notFound.postValue(true);
            }
        });
    }

    public void getPending(String token,
                           MutableLiveData<List<MatchResponse>> result,
                           MutableLiveData<String> error,
                           MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getPending(token).enqueue(
                new ApiCallback<List<MatchResponse>>(loading, error) {
                    @Override
                    protected void onSuccess(List<MatchResponse> body) {
                        result.postValue(body);
                    }
                });
    }

    public void getCompleted(String token,
                           MutableLiveData<List<MatchResponse>> result,
                           MutableLiveData<String> error,
                           MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getCompleted(token).enqueue(
                new ApiCallback<List<MatchResponse>>(loading, error) {
                    @Override
                    protected void onSuccess(List<MatchResponse> body) {
                        result.postValue(body);
                    }
                });
    }
}
