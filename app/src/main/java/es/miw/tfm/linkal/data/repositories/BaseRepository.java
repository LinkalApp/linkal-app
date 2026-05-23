package es.miw.tfm.linkal.data.repositories;

import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Clase base para todos los repositories.
 * Centraliza el manejo común de callbacks Retrofit: desactivar loading,
 * publicar errores HTTP y errores de red.
 */
public abstract class BaseRepository {
    protected abstract static class ApiCallback<T> implements Callback<T> {

        private final MutableLiveData<Boolean> loading;
        private final MutableLiveData<String> error;

        protected ApiCallback(MutableLiveData<Boolean> loading,
                              MutableLiveData<String> error) {
            this.loading = loading;
            this.error = error;
        }

        /** Lógica a ejecutar cuando la respuesta HTTP es exitosa (2xx). */
        protected abstract void onSuccess(T body);

        /** Permite a las subclases publicar un error personalizado si lo necesitan. */
        protected void postError(String message) {
            error.postValue(message);
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            loading.postValue(false);
            if (response.isSuccessful()) {
                onSuccess(response.body());
            } else {
                error.postValue("Error " + response.code() + ": " + response.message());
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            loading.postValue(false);
            error.postValue("Error de conexión: " + t.getMessage());
        }
    }
}
