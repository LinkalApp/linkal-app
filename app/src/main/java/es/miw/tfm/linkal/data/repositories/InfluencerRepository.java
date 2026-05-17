package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.InfluencerApiService;
import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfluencerRepository {
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
        apiService.register(request).enqueue(new Callback<Void>() {
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
