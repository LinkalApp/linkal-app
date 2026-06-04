package es.miw.tfm.linkal.viewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.miw.tfm.linkal.data.repositories.InfluencerRepository;
import es.miw.tfm.linkal.models.requests.RegisterInfluencerRequest;
import es.miw.tfm.linkal.models.requests.UpdateInfluencerRequest;

@RunWith(MockitoJUnitRunner.class)
public class InfluencerViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private InfluencerRepository mockRepository;
    private InfluencerViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new InfluencerViewModel(mockRepository);
    }

    @Test
    public void isLoading_initialValue_isFalse() {
        Boolean value = viewModel.getIsLoading().getValue();
        assertNotNull(value);
        assertFalse(value);
    }

    @Test
    public void registerSuccess_initialValue_isNull() {
        assertNull(viewModel.getRegisterSuccess().getValue());
    }

    @Test
    public void errorMessage_initialValue_isNull() {
        assertNull(viewModel.getErrorMessage().getValue());
    }

    @Test
    public void updateSuccess_initialValue_isNull() {
        assertNull(viewModel.getUpdateSuccess().getValue());
    }

    @Test
    public void profile_initialValue_isNull() {
        assertNull(viewModel.getProfile().getValue());
    }

    @Test
    public void deleteSuccess_initialValue_isNull() {
        assertNull(viewModel.getDeleteSuccess().getValue());
    }


    @Test
    public void getIsLoading_returnsLiveData() {
        assertNotNull(viewModel.getIsLoading());
    }

    @Test
    public void getRegisterSuccess_returnsLiveData() {
        assertNotNull(viewModel.getRegisterSuccess());
    }

    @Test
    public void getErrorMessage_returnsLiveData() {
        assertNotNull(viewModel.getErrorMessage());
    }

    @Test
    public void getProfile_returnsLiveData() {
        assertNotNull(viewModel.getProfile());
    }

    @Test
    public void getUpdateSuccess_returnsLiveData() {
        assertNotNull(viewModel.getUpdateSuccess());
    }

    @Test
    public void getDeleteSuccess_returnsLiveData() {
        assertNotNull(viewModel.getDeleteSuccess());
    }

    // Register -----------------------------------------------------------------------------------
    @Test
    public void register_delegatesToRepository() {
        RegisterInfluencerRequest request = new RegisterInfluencerRequest(
                "Laura", "laura@test.com", "Pass1.", "666000000",
                "Bio", "ArtName", null, null, null, null);

        viewModel.register(request);

        verify(mockRepository).register(eq(request), any(), any(), any());
    }

    // loadProfile --------------------------------------------------------------------------------

    @Test
    public void loadProfile_delegatesToRepository() {
        viewModel.loadProfile("Bearer my-token");

        verify(mockRepository).getProfile(eq("Bearer my-token"), any(), any(), any());
    }

    @Test
    public void loadProfile_withDifferentToken_passesItToRepository() {
        viewModel.loadProfile("Bearer other-token");

        verify(mockRepository).getProfile(eq("Bearer other-token"), any(), any(), any());
    }

    // updateProfile ------------------------------------------------------------------------------

    @Test
    public void updateProfile_delegatesToRepository() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest(
                null, null, null, null, null,
                "@ig", "@tt", null
        );

        viewModel.updateProfile("Bearer my-token", request);

        verify(mockRepository).updateProfile(eq("Bearer my-token"), eq(request), any(), any(), any());
    }

    @Test
    public void updateProfile_withDifferentToken_passesItToRepository() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest(
                null, null, null, null, null,
                "@ig", null, null
        );

        viewModel.updateProfile("Bearer other-token", request);

        verify(mockRepository).updateProfile(eq("Bearer other-token"), eq(request), any(), any(), any());
    }

    @Test
    public void updateProfile_withFullRequest_delegatesAllFields() {
        UpdateInfluencerRequest request = new UpdateInfluencerRequest(
                "Laura", "600000000", "Bio", "LauraStyle",
                null, "@ig", "@tt", "YT"
        );

        viewModel.updateProfile("Bearer token", request);

        verify(mockRepository).updateProfile(eq("Bearer token"), eq(request), any(), any(), any());
    }

    // deleteAccount --------------------------------------------------------------------------------

    @Test
    public void deleteAccount_delegatesToRepository() {
        viewModel.deleteAccount("Bearer my-token");

        verify(mockRepository).deleteAccount(eq("Bearer my-token"), any(), any(), any());
    }

    @Test
    public void deleteAccount_withDifferentToken_passesItToRepository() {
        viewModel.deleteAccount("Bearer other-token");

        verify(mockRepository).deleteAccount(eq("Bearer other-token"), any(), any(), any());
    }

    // loadAll ------------------------------------------------------------------------------------

    @Test
    public void loadAll_delegatesToRepository() {
        viewModel.loadAll("Bearer token");

        verify(mockRepository).getAll(eq("Bearer token"), any(), any(), any());
    }

    @Test
    public void loadAll_withDifferentToken_passesItToRepository() {
        viewModel.loadAll("Bearer other-token");

        verify(mockRepository).getAll(eq("Bearer other-token"), any(), any(), any());
    }

    @Test
    public void getInfluencers_returnsLiveData() {
        assertNotNull(viewModel.getInfluencers());
    }

    @Test
    public void influencers_initialValue_isNull() {
        assertNull(viewModel.getInfluencers().getValue());
    }

    // loadByInterests -----------------------------------------------------

    @Test
    public void loadByInterests_withInterests_delegatesToRepository() {
        List<String> interests = Arrays.asList("Moda", "Belleza");

        viewModel.loadByInterests("Bearer token", interests);

        verify(mockRepository).getByInterests(eq("Bearer token"), eq(interests), any(), any(), any());
    }

    @Test
    public void loadByInterests_withDifferentToken_passesItToRepository() {
        List<String> interests = Arrays.asList("Moda");

        viewModel.loadByInterests("Bearer other-token", interests);

        verify(mockRepository).getByInterests(eq("Bearer other-token"), eq(interests), any(), any(), any());
    }

    @Test
    public void loadByInterests_withEmptyList_callsLoadAll() {
        viewModel.loadByInterests("Bearer token", Collections.emptyList());

        verify(mockRepository).getAll(eq("Bearer token"), any(), any(), any());
        verify(mockRepository, never()).getByInterests(any(), any(), any(), any(), any());
    }

    @Test
    public void loadByInterests_withNullList_callsLoadAll() {
        viewModel.loadByInterests("Bearer token", null);

        verify(mockRepository).getAll(eq("Bearer token"), any(), any(), any());
        verify(mockRepository, never()).getByInterests(any(), any(), any(), any(), any());
    }

    @Test
    public void loadByInterests_doesNotCallGetAll_whenInterestsPresent() {
        viewModel.loadByInterests("Bearer token", Arrays.asList("Viajes"));

        verify(mockRepository, never()).getAll(any(), any(), any(), any());
    }
}
