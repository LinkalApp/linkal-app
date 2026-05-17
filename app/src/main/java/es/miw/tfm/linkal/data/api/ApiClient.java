package es.miw.tfm.linkal.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //private static final String BASE_URL = "https://ec2-51-48-43-213.eu-south-2.compute.amazonaws.com/api/";
    public static final String BASE_URL = "http://10.0.2.2:9090/api/";

    private static Retrofit retrofit;

    private ApiClient() {}

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static InfluencerApiService getInfluencerApiService() {
        return getInstance().create(InfluencerApiService.class);
    }

    public static BusinessApiService getBusinessApiService() {
        return getInstance().create(BusinessApiService.class);
    }
}
