package test.eoin.com.vctest.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private final static String TAG = RestClient.class.getSimpleName();
    private static RestInterface REST_CLIENT;
    private static final String BASE_URL = "https://api.themoviedb.org";

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static RestInterface get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(8, TimeUnit.SECONDS);
        httpClient.connectTimeout(8, TimeUnit.SECONDS);

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        REST_CLIENT = builder.create(RestInterface.class);
    }
}

