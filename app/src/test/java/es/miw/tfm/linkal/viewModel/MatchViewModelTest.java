package es.miw.tfm.linkal.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.repositories.MatchRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MatchViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private MatchRepository mockMatchRepository;

    private MatchViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new MatchViewModel(mockMatchRepository);
    }

    // Estado inicial -----------------------------------------------------

    @Test
    public void matchResult_initialValue_isNull() {
        assertNull(viewModel.getMatchResult().getValue());
    }

    @Test
    public void existingMatch_initialValue_isNull() {
        assertNull(viewModel.getExistingMatch().getValue());
    }

    @Test
    public void matchNotFound_initialValue_isNull() {
        assertNull(viewModel.getMatchNotFound().getValue());
    }

    @Test
    public void errorMessage_initialValue_isNull() {
        assertNull(viewModel.getError().getValue());
    }

    // Getters LiveData -------------------------------------------------

    @Test
    public void getMatchResult_returnsLiveData() {
        assertNotNull(viewModel.getMatchResult());
    }

    @Test
    public void getExistingMatch_returnsLiveData() {
        assertNotNull(viewModel.getExistingMatch());
    }

    @Test
    public void getMatchNotFound_returnsLiveData() {
        assertNotNull(viewModel.getMatchNotFound());
    }

    @Test
    public void getErrorMessage_returnsLiveData() {
        assertNotNull(viewModel.getError());
    }

    // createByInfluencer → MatchRepository -------------------------------------------------

    @Test
    public void createByInfluencer_delegatesToRepository() {
        viewModel.createByInfluencer("Bearer token", "campaign-id");

        verify(mockMatchRepository).createByInfluencer(eq("Bearer token"), eq("campaign-id"), any(), any(), any());
    }

    @Test
    public void createByInfluencer_withDifferentToken_passesItToRepository() {
        viewModel.createByInfluencer("Bearer other-token", "campaign-id");

        verify(mockMatchRepository).createByInfluencer(eq("Bearer other-token"), eq("campaign-id"), any(), any(), any());
    }

    @Test
    public void createByInfluencer_withDifferentCampaignId_passesItToRepository() {
        viewModel.createByInfluencer("Bearer token", "other-campaign-id");

        verify(mockMatchRepository).createByInfluencer(eq("Bearer token"), eq("other-campaign-id"), any(), any(), any());
    }

    @Test
    public void createByInfluencer_doesNotCallFind() {
        viewModel.createByInfluencer("Bearer token", "campaign-id");

        verify(mockMatchRepository, never()).findByInfluencer(any(), any(), any(), any());
    }

    // createByBusiness → MatchRepository --------------------------------------------------------------

    @Test
    public void createByBusiness_delegatesToRepository() {
        viewModel.createByBusiness("Bearer token", "influencer-id", "campaign-id");

        verify(mockMatchRepository).createByBusiness(
                eq("Bearer token"), eq("influencer-id"), eq("campaign-id"), any(), any(), any());
    }

    @Test
    public void createByBusiness_withDifferentInfluencerId_passesItToRepository() {
        viewModel.createByBusiness("Bearer token", "other-influencer-id", "campaign-id");

        verify(mockMatchRepository).createByBusiness(
                eq("Bearer token"), eq("other-influencer-id"), eq("campaign-id"), any(), any(), any());
    }

    @Test
    public void createByBusiness_withDifferentCampaignId_passesItToRepository() {
        viewModel.createByBusiness("Bearer token", "influencer-id", "other-campaign-id");

        verify(mockMatchRepository).createByBusiness(
                eq("Bearer token"), eq("influencer-id"), eq("other-campaign-id"), any(), any(), any());
    }

    @Test
    public void createByBusiness_doesNotCallCreateByInfluencer() {
        viewModel.createByBusiness("Bearer token", "influencer-id", "campaign-id");

        verify(mockMatchRepository, never()).createByInfluencer(any(), any(), any(), any(), any());
    }

    @Test
    public void createByBusiness_doesNotCallFind() {
        viewModel.createByBusiness("Bearer token", "influencer-id", "campaign-id");

        verify(mockMatchRepository, never()).findByInfluencer(any(), any(), any(), any());
    }

    // findByInfluencer → MatchRepository ------------------------------------------------

    @Test
    public void findByInfluencer_delegatesToRepository() {
        viewModel.findByInfluencer("Bearer token", "campaign-id");

        verify(mockMatchRepository).findByInfluencer(eq("Bearer token"), eq("campaign-id"), any(), any());
    }

    @Test
    public void findByInfluencer_withDifferentToken_passesItToRepository() {
        viewModel.findByInfluencer("Bearer other-token", "campaign-id");

        verify(mockMatchRepository).findByInfluencer(eq("Bearer other-token"), eq("campaign-id"), any(), any());
    }

    @Test
    public void findByInfluencer_doesNotCallCreate() {
        viewModel.findByInfluencer("Bearer token", "campaign-id");

        verify(mockMatchRepository, never()).createByInfluencer(any(), any(), any(), any(), any());
    }

    // loadPending → MatchRepository ---------------------------------------------------

    @Test
    public void loadPending_delegatesToRepository() {
        viewModel.loadPending("Bearer token");

        verify(mockMatchRepository).getPending(eq("Bearer token"), any(), any(), any());
    }

    @Test
    public void loadPending_withDifferentToken_passesItToRepository() {
        viewModel.loadPending("Bearer other-token");

        verify(mockMatchRepository).getPending(eq("Bearer other-token"), any(), any(), any());
    }

    @Test
    public void loadPending_doesNotCallCreate() {
        viewModel.loadPending("Bearer token");

        verify(mockMatchRepository, never()).createByInfluencer(any(), any(), any(), any(), any());
        verify(mockMatchRepository, never()).createByBusiness(any(), any(), any(), any(), any(), any());
    }
}
