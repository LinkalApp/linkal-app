package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import es.miw.tfm.linkal.data.api.AdminApiService;
import es.miw.tfm.linkal.data.api.ApiClient;
import es.miw.tfm.linkal.models.responses.AdminUserResponse;

public class AdminRepository extends BaseRepository{
    private static AdminRepository instance;
    private final AdminApiService apiService;

    private AdminRepository() {
        this.apiService = ApiClient.getAdminApiService();
    }

    /** Constructor package-private para inyección en tests. */
    AdminRepository(AdminApiService apiService) {
        this.apiService = apiService;
    }

    public static AdminRepository getInstance() {
        if (instance == null) {
            instance = new AdminRepository();
        }
        return instance;
    }

    public void findAll(String token,
                        String role,
                        Boolean verified,
                        MutableLiveData<List<AdminUserResponse>> result,
                        MutableLiveData<String> error,
                        MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.findAll(token, role, verified).enqueue(
                new ApiCallback<List<AdminUserResponse>>(loading, error) {
                    @Override
                    protected void onSuccess(List<AdminUserResponse> body) {
                        result.postValue(body);
                    }
                });
    }

    public void findById(String token,
                         String id,
                         MutableLiveData<AdminUserResponse> result,
                         MutableLiveData<String> error,
                         MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.findById(token, id).enqueue(
                new ApiCallback<AdminUserResponse>(loading, error) {
                    @Override
                    protected void onSuccess(AdminUserResponse body) {
                        result.postValue(body);
                    }
                });
    }

    public void verifyUser(String token,
                           String id,
                           MutableLiveData<AdminUserResponse> result,
                           MutableLiveData<String> error,
                           MutableLiveData<Boolean> loading) {
        loading.setValue(true);
        apiService.verifyUser(token, id).enqueue(
                new ApiCallback<AdminUserResponse>(loading, error) {
                    @Override
                    protected void onSuccess(AdminUserResponse body) {
                        result.postValue(body);
                    }
                });
    }
}
