package es.miw.tfm.linkal.viewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.repositories.AuthRepository;
import es.miw.tfm.linkal.data.repositories.AuthRepositoryTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserViewModelTest {

    @Mock
    private AuthRepository mockAuthRepository;

    private UserViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new UserViewModel(mockAuthRepository);
    }

    // Estado inicial de los LiveData ---------------------------------------------

    @Test
    public void loading_initialValue_isFalse() {
        Boolean value = viewModel.getLoading().getValue();
        assertNotNull(value);
        assertFalse(value);
    }

    @Test
    public void authResult_initialValue_isNull() {
        assertNull(viewModel.getAuthResult().getValue());
    }

    @Test
    public void error_initialValue_isNull() {
        assertNull(viewModel.getError().getValue());
    }

    // Los getters devuelven LiveData no nulos ---------------------------------------------

    @Test
    public void getLoading_returnsLiveData() {
        assertNotNull(viewModel.getLoading());
    }

    @Test
    public void getAuthResult_returnsLiveData() {
        assertNotNull(viewModel.getAuthResult());
    }

    @Test
    public void getError_returnsLiveData() {
        assertNotNull(viewModel.getError());
    }

    // login() delega en el repository ------------------------------------------

    @Test
    public void login_delegatesToRepository() {
        viewModel.login("user@test.com", "pass123");

        verify(mockAuthRepository).login(
                eq("user@test.com"),
                eq("pass123"),
                any(), any(), any()
        );
    }

    @Test
    public void login_withDifferentCredentials_passesThemToRepository() {
        viewModel.login("influencer@linkal.es", "secret99");

        verify(mockAuthRepository).login(
                eq("influencer@linkal.es"),
                eq("secret99"),
                any(), any(), any()
        );
    }
}
