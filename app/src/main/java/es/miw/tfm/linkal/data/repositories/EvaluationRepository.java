package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.data.api.EvaluationApiService;
import es.miw.tfm.linkal.models.requests.EvaluationRequest;
import es.miw.tfm.linkal.models.responses.EvaluationResponse;

public class EvaluationRepository extends BaseRepository{
    private static EvaluationRepository instance;
    private final EvaluationApiService apiService;

    private EvaluationRepository() {
        this.apiService = ApiClient.getEvaluationApiService();
    }

    EvaluationRepository(EvaluationApiService apiService) {
        this.apiService = apiService;
    }

    public static EvaluationRepository getInstance() {
        if (instance == null) {
            instance = new EvaluationRepository();
        }
        return instance;
    }

    public void create(String token,
                       String matchId,
                       EvaluationRequest request,
                       MutableLiveData<EvaluationResponse> result,
                       MutableLiveData<String> error,
                       MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.create(token, matchId, request).enqueue(
                new ApiCallback<EvaluationResponse>(loading, error) {
                    @Override
                    protected void onSuccess(EvaluationResponse body) {
                        result.postValue(body);
                    }
                });
    }
}
