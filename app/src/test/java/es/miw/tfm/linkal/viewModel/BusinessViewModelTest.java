package es.miw.tfm.linkal.viewModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BusinessViewModelTest {

    private BusinessViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new BusinessViewModel();
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
}