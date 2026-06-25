package es.miw.tfm.linkal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.miw.tfm.linkal.data.repositories.AdminRepository;
import es.miw.tfm.linkal.models.responses.AdminUserResponse;

public class AdminViewModel extends ViewModel {
    private final AdminRepository adminRepository;

    private final MutableLiveData<List<AdminUserResponse>> usersResult = new MutableLiveData<>();
    private final MutableLiveData<AdminUserResponse> userDetail  = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading   = new MutableLiveData<>(false);
    private final MutableLiveData<AdminUserResponse> verifyResult  = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteResult  = new MutableLiveData<>();


    /** Constructor por defecto — usa el singleton de producción. */
    public AdminViewModel() {
        this.adminRepository = AdminRepository.getInstance();
    }

    /** Constructor para tests — permite inyectar un mock. */
    AdminViewModel(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    public void loadAll(String token, String role, Boolean verified) {
        adminRepository.findAll(token, role, verified, usersResult, errorMessage, isLoading);
    }

    public void loadById(String token, String id) {
        adminRepository.findById(token, id, userDetail, errorMessage, isLoading);
    }

    public void verifyUser(String token, String id) {
        adminRepository.verifyUser(token, id, verifyResult, errorMessage, isLoading);
    }

    public void deleteUser(String token, String id) {
        adminRepository.deleteUser(token, id, deleteResult, errorMessage, isLoading);
    }

    public LiveData<List<AdminUserResponse>> getUsersResult() { return usersResult; }
    public LiveData<AdminUserResponse> getUserDetail()  { return userDetail; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<AdminUserResponse> getVerifyResult() { return verifyResult; }
    public LiveData<Boolean> getDeleteResult() { return deleteResult; }


}
