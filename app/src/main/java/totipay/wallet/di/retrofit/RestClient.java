package totipay.wallet.di.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String Google_BASE_AutoComplete_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/";
//    private static final String base_url = "http://betaapplication.com/workerzapp/backend/web/index.php/api/";

    // live server URL
//    https://m.myworkerapp.com
    public static final String base_url = "https://183.87.134.37/TotipayImagesApi/";

    //MYWorkerappz
//    private static final String base_url = "http://betaapplication.com/myworkerapp/backend/web/index.php/api/";

    //    private static final String base_url = "http://180.179.114.39/backend/web/index.php/api/";
//    live
//    private static final String base_url = "https://m.workerappz.com/backend/web/index.php/api/";
    private static RestApi REST_CLIENT;
    private static Retrofit restAdapter;
    private static Retrofit retrofit_google = null;

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
