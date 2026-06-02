package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.InfluencerApiService;
import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;
import es.miw.tfm.linkal.models.responses.InfluencerProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfluencerRepository extends BaseRepository {
    private static InfluencerRepository instance;
    private final InfluencerApiService apiService;

    private InfluencerRepository() {
        this.apiService = ApiClient.getInfluencerApiService();
    }

    /** Constructor package-private para inyección en tests. */
    InfluencerRepository(InfluencerApiService apiService) {
        this.apiService = apiService;
    }

    public static InfluencerRepository getInstance() {
        if (instance == null) {
            instance = new InfluencerRepository();
        }
        return instance;
    }

    public void register(RegisterInfluencerRequest request,
                         MutableLiveData<Boolean> success,
                         MutableLiveData<String> error,
                         MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.register(request).enqueue(
                new ApiCallback<Void>(loading, error) {
                    @Override
                    protected void onSuccess(Void body) {
                        success.postValue(true);
                    }
                });
    }

    public void getProfile(String token,
                           MutableLiveData<InfluencerProfileResponse> profileResult,
                           MutableLiveData<String> error,
                           MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getProfile(token).enqueue(
                new ApiCallback<InfluencerProfileResponse>(loading, error) {
                    @Override
                    protected void onSuccess(InfluencerProfileResponse body) {
                        profileResult.postValue(body);
                    }
                });
    }

    public void updateProfile(String token,
                              UpdateInfluencerRequest request,
                              MutableLiveData<InfluencerProfileResponse> profileResult,
                              MutableLiveData<String> error,
                              MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.updateProfile(token, request).enqueue(
                new ApiCallback<InfluencerProfileResponse>(loading, error) {
                    @Override
                    protected void onSuccess(InfluencerProfileResponse body) {
                        profileResult.postValue(body);
                    }
                });
    }

    public void deleteAccount(String token,
                              MutableLiveData<Boolean> deleteSuccess,
                              MutableLiveData<String> error,
                              MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.deleteAccount(token).enqueue(
                new ApiCallback<Void>(loading, error) {
                    @Override
                    protected void onSuccess(Void body) {
                        deleteSuccess.postValue(true);
                    }
                });
    }
    public void getAll(String token,
                       MutableLiveData<List<InfluencerProfileResponse>> result,
                       MutableLiveData<String> error,
                       MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getAll(token).enqueue(
                new ApiCallback<List<InfluencerProfileResponse>>(loading, error) {
                    @Override
                    protected void onSuccess(List<InfluencerProfileResponse> body) {
                        result.postValue(body);
                    }
                });
    }
}
