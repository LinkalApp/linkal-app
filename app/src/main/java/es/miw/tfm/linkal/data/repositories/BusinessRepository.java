package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.BusinessApiService;
import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import es.miw.tfm.linkal.models.responses.BusinessProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessRepository extends BaseRepository{
    private static BusinessRepository instance;
    private final BusinessApiService apiService;

    private BusinessRepository() {
        this.apiService = ApiClient.getBusinessApiService();
    }

    /** Constructor package-private para inyección en tests. */
    BusinessRepository(BusinessApiService apiService) {
        this.apiService = apiService;
    }

    public static BusinessRepository getInstance() {
        if (instance == null) {
            instance = new BusinessRepository();
        }
        return instance;
    }

    public void register(RegisterBusinessRequest request,
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
                           MutableLiveData<BusinessProfileResponse> profile,
                           MutableLiveData<String> error,
                           MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.getProfile(token).enqueue(
                new ApiCallback<BusinessProfileResponse>(loading, error) {
                    @Override
                    protected void onSuccess(BusinessProfileResponse body) {
                        profile.postValue(body);
                    }
                });
    }
}
