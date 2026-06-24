package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.miw.tfm.linkal.data.repositories.EvaluationRepository;
import es.miw.tfm.linkal.models.requests.EvaluationRequest;
import es.miw.tfm.linkal.models.responses.EvaluationResponse;

public class EvaluationViewModel extends ViewModel {
    private final MutableLiveData<EvaluationResponse> evaluationResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private final EvaluationRepository repository;

    public EvaluationViewModel() {
        this.repository = EvaluationRepository.getInstance();
    }

    EvaluationViewModel(EvaluationRepository repository) {
        this.repository = repository;
    }

    public void create(String token, String matchId, int score) {
        repository.create(token, matchId, new EvaluationRequest(score),
                evaluationResult, errorMessage, isLoading);
    }

    public void createByInfluencer(String token, String matchId, int score) {
        repository.createByInfluencer(token, matchId, new EvaluationRequest(score),
                evaluationResult, errorMessage, isLoading);
    }

    public LiveData<EvaluationResponse> getEvaluationResult() { return evaluationResult; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
}
