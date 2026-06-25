package es.miw.tfm.linkal.data.api;

import java.util.List;

import es.miw.tfm.linkal.models.responses.AdminUserResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminApiService {
    // RUTA DEL CONTROLADOR ADMIn
    String base = "admin";
    @GET(base + "/users")
    Call<List<AdminUserResponse>> findAll( @Header("Authorization") String token,
                                           @Query("role") String role,
                                           @Query("verified") Boolean verified);

    @GET(base + "/users/{id}")
    Call<AdminUserResponse> findById( @Header("Authorization") String token,
                                      @Path("id") String id);

    @PATCH(base + "/users/{id}/verify")
    Call<AdminUserResponse> verifyUser( @Header("Authorization") String token,
                                        @Path("id") String id);

    @DELETE(base + "/users/{id}")
    Call<Void> deleteUser( @Header("Authorization") String token,
                           @Path("id") String id);
}
