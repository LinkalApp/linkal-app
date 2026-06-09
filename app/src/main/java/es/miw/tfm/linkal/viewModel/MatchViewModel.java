package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.miw.tfm.linkal.data.repositories.MatchRepository;
import es.miw.tfm.linkal.models.responses.MatchResponse;

public class MatchViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<MatchResponse> matchResult = new MutableLiveData<>();
    private final MutableLiveData<MatchResponse> existingMatch  = new MutableLiveData<>();
    private final MutableLiveData<Boolean> matchNotFound = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading  = new MutableLiveData<>(false);

    private final MatchRepository matchRepository;

    public MatchViewModel() {
        this.matchRepository = MatchRepository.getInstance();
    }

    MatchViewModel(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public void createByInfluencer(String token, String campaignId) {
        matchRepository.createByInfluencer(token, campaignId, matchResult, errorMessage, loading);
    }

    public void findByInfluencer(String token, String campaignId) {
        matchRepository.findByInfluencer(token, campaignId, existingMatch, matchNotFound);
    }

    public LiveData<MatchResponse> getMatchResult() { return matchResult; }
    public LiveData<MatchResponse> getExistingMatch() { return existingMatch; }
    public LiveData<Boolean> getMatchNotFound() { return matchNotFound; }
    public LiveData<String> getError() { return errorMessage; }
}
