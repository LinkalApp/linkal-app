package es.miw.tfm.linkal.data.api;

import es.miw.tfm.linkal.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = BuildConfig.BASE_URL;
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

    public static AuthApiService getAuthApiService() {
        return getInstance().create(AuthApiService.class);
    }

    public static CampaignApiService getCampaignApiService() {
        return getInstance().create(CampaignApiService.class);
    }

    public static MatchApiService getMatchApiService() {
        return getInstance().create(MatchApiService.class);
    }

    public static ChatApiService getChatApiService() {
        return getInstance().create(ChatApiService.class);
    }

    public static EvaluationApiService getEvaluationApiService() {
        return getInstance().create(EvaluationApiService.class);
    }
}
