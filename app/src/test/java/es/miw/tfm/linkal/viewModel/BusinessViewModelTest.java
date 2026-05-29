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
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import es.miw.tfm.linkal.data.repositories.BusinessRepository;
import es.miw.tfm.linkal.models.requests.UpdateBusinessRequest;

@RunWith(MockitoJUnitRunner.class)
public class BusinessViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private BusinessRepository mockRepository;
    private BusinessViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new BusinessViewModel(mockRepository);
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
    public void profile_initialValue_isNull() {
        assertNull(viewModel.getProfile().getValue());
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

    // updateProfile -------------------------------------------------------------------------------

    @Test
    public void updateProfile_delegatesToRepository() {
        UpdateBusinessRequest request = new UpdateBusinessRequest(
                "Empresa SA", "611000000", "Bio",
                "Calle Mayor 1", "Madrid", "https://empresa.com");

        viewModel.updateProfile("Bearer my-token", request);

        verify(mockRepository).updateProfile(eq("Bearer my-token"), eq(request), any(), any(), any());
    }

    @Test
    public void updateProfile_withDifferentToken_passesItToRepository() {
        UpdateBusinessRequest request = new UpdateBusinessRequest(
                "Empresa SA", "611000000", "Bio",
                "Calle Mayor 1", "Madrid", "https://empresa.com");

        viewModel.updateProfile("Bearer other-token", request);

        verify(mockRepository).updateProfile(eq("Bearer other-token"), eq(request), any(), any(), any());
    }
}