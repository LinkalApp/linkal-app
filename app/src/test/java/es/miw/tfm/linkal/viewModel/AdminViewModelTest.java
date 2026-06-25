package es.miw.tfm.linkal.viewModel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import es.miw.tfm.linkal.data.repositories.AdminRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AdminViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private AdminRepository mockAdminRepository;

    private AdminViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new AdminViewModel(mockAdminRepository);
    }

    // Estado inicial ------------------------------------------------------------

    @Test
    public void usersResult_initialValue_isNull() {
        assertNull(viewModel.getUsersResult().getValue());
    }

    @Test
    public void userDetail_initialValue_isNull() {
        assertNull(viewModel.getUserDetail().getValue());
    }

    @Test
    public void errorMessage_initialValue_isNull() {
        assertNull(viewModel.getErrorMessage().getValue());
    }

    @Test
    public void isLoading_initialValue_isFalse() {
        assertFalse(Boolean.TRUE.equals(viewModel.getIsLoading().getValue()));
    }

    // Getters LiveData ---------------------------------------------------------------------

    @Test
    public void getUsersResult_returnsLiveData() {
        assertNotNull(viewModel.getUsersResult());
    }

    @Test
    public void getUserDetail_returnsLiveData() {
        assertNotNull(viewModel.getUserDetail());
    }

    @Test
    public void getErrorMessage_returnsLiveData() {
        assertNotNull(viewModel.getErrorMessage());
    }

    @Test
    public void getIsLoading_returnsLiveData() {
        assertNotNull(viewModel.getIsLoading());
    }

    // loadAll -----------------------------------------------------------------------------

    @Test
    public void loadAll_delegatesToRepository() {
        viewModel.loadAll("Bearer token", null, null);

        verify(mockAdminRepository).findAll(
                eq("Bearer token"), eq(null), eq(null), any(), any(), any());
    }

    @Test
    public void loadAll_withRoleFilter_passesRoleToRepository() {
        viewModel.loadAll("Bearer token", "INFLUENCER", null);

        verify(mockAdminRepository).findAll(
                any(), eq("INFLUENCER"), eq(null), any(), any(), any());
    }

    @Test
    public void loadAll_withVerifiedFilter_passesVerifiedToRepository() {
        viewModel.loadAll("Bearer token", null, true);

        verify(mockAdminRepository).findAll(
                any(), eq(null), eq(true), any(), any(), any());
    }

    @Test
    public void loadAll_withBothFilters_passesBothToRepository() {
        viewModel.loadAll("Bearer token", "BUSINESS", false);

        verify(mockAdminRepository).findAll(
                any(), eq("BUSINESS"), eq(false), any(), any(), any());
    }

    @Test
    public void loadAll_doesNotCallFindById() {
        viewModel.loadAll("Bearer token", null, null);

        verify(mockAdminRepository, never()).findById(any(), any(), any(), any(), any());
    }

    // loadById -----------------------------------------------------------------------

    @Test
    public void loadById_delegatesToRepository() {
        viewModel.loadById("Bearer token", "user-id-123");

        verify(mockAdminRepository).findById(
                eq("Bearer token"), eq("user-id-123"), any(), any(), any());
    }

    @Test
    public void loadById_withDifferentToken_passesItToRepository() {
        viewModel.loadById("Bearer other-token", "user-id-123");

        verify(mockAdminRepository).findById(
                eq("Bearer other-token"), any(), any(), any(), any());
    }

    @Test
    public void loadById_withDifferentId_passesItToRepository() {
        viewModel.loadById("Bearer token", "other-user-id");

        verify(mockAdminRepository).findById(
                any(), eq("other-user-id"), any(), any(), any());
    }

    @Test
    public void loadById_doesNotCallFindAll() {
        viewModel.loadById("Bearer token", "user-id");

        verify(mockAdminRepository, never()).findAll(any(), any(), any(), any(), any(), any());
    }

    // verifyUser -------------------------------------------------------------------------------

    @Test
    public void verifyResult_initialValue_isNull() {
        assertNull(viewModel.getVerifyResult().getValue());
    }

    @Test
    public void getVerifyResult_returnsLiveData() {
        assertNotNull(viewModel.getVerifyResult());
    }

    @Test
    public void verifyUser_delegatesToRepository() {
        viewModel.verifyUser("Bearer token", "user-id-123");

        verify(mockAdminRepository).verifyUser(
                eq("Bearer token"), eq("user-id-123"), any(), any(), any());
    }

    @Test
    public void verifyUser_withDifferentId_passesIdToRepository() {
        viewModel.verifyUser("Bearer token", "other-id");

        verify(mockAdminRepository).verifyUser(
                any(), eq("other-id"), any(), any(), any());
    }

    @Test
    public void verifyUser_doesNotCallFindAll() {
        viewModel.verifyUser("Bearer token", "user-id");

        verify(mockAdminRepository, never()).findAll(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void verifyUser_doesNotCallFindById() {
        viewModel.verifyUser("Bearer token", "user-id");

        verify(mockAdminRepository, never()).findById(any(), any(), any(), any(), any());
    }

    // deleteUser ---------------------------------------------------------------------------------

    @Test
    public void deleteResult_initialValue_isNull() {
        assertNull(viewModel.getDeleteResult().getValue());
    }

    @Test
    public void getDeleteResult_returnsLiveData() {
        assertNotNull(viewModel.getDeleteResult());
    }

    @Test
    public void deleteUser_delegatesToRepository() {
        viewModel.deleteUser("Bearer token", "user-id-123");

        verify(mockAdminRepository).deleteUser(
                eq("Bearer token"), eq("user-id-123"), any(), any(), any());
    }

    @Test
    public void deleteUser_withDifferentId_passesIdToRepository() {
        viewModel.deleteUser("Bearer token", "other-user-id");

        verify(mockAdminRepository).deleteUser(
                any(), eq("other-user-id"), any(), any(), any());
    }

    @Test
    public void deleteUser_withDifferentToken_passesTokenToRepository() {
        viewModel.deleteUser("Bearer other-token", "user-id-123");

        verify(mockAdminRepository).deleteUser(
                eq("Bearer other-token"), any(), any(), any(), any());
    }

    @Test
    public void deleteUser_doesNotCallFindAll() {
        viewModel.deleteUser("Bearer token", "user-id-123");

        verify(mockAdminRepository, never()).findAll(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void deleteUser_doesNotCallFindById() {
        viewModel.deleteUser("Bearer token", "user-id-123");

        verify(mockAdminRepository, never()).findById(any(), any(), any(), any(), any());
    }

    @Test
    public void deleteUser_doesNotCallVerifyUser() {
        viewModel.deleteUser("Bearer token", "user-id-123");

        verify(mockAdminRepository, never()).verifyUser(any(), any(), any(), any(), any());
    }
}