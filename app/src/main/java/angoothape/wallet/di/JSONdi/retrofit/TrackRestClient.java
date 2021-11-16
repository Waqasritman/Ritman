package angoothape.wallet.di.JSONdi.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackRestClient {
    static {
        System.loadLibrary("native-lib");
    }

    public static native String baseURLJIN();
    public static native String baseKEY();


    private static RestApi REST_CLIENT;
    private static Retrofit restAdapter;
    public static final String base_url = baseURLJIN();
    static {
        setupRestClient();
    }

    private static void setupRestClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000,TimeUnit.SECONDS);
//        builder.connectTimeout(300, TimeUnit.SECONDS);
//        builder.readTimeout(80, TimeUnit.SECONDS);
//        builder.writeTimeout(90, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().setLenient().create();
        restAdapter = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    public static RestApi get() {
        if (REST_CLIENT == null) {
            REST_CLIENT = restAdapter.create(RestApi.class);
        }
        return REST_CLIENT;
    }
}
