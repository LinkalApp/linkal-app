package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.BusinessApiService;
import es.miw.tfm.linkal.models.requests.RegisterBusinessRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessRepository {
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
        apiService.register(request).enqueue(new Callback<Void>() { // ← apiService, no ApiClient
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loading.postValue(false);
                if (response.isSuccessful()) {
                    success.postValue(true);
                } else {
                    error.postValue("Error " + response.code() + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.postValue(false);
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
