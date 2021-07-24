package ritman.wallet.di.JSONdi.retrofit;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ritman.wallet.di.JSONdi.retrofit.RestApi;

public class RestClient {

    //public static final String base_url = "https://183.87.134.37/TotipayImagesApi/";
    //public static final String base_url = "https://183.87.134.37/RitPayDemoImagesApiUAT/RitmanPay/Customer/";

    public static final String RitpayDomestic_base_url = "https://183.87.134.37/RitpayDomesticRestAPIUAT/RitmanPay/";

    public static final String RitpayDomestic_live_base_url = "https://payinapi.angoothape.com/RitmanPay/";

    public static final String IndiaFirst_url = "https://183.87.134.37/RitpayDomesticRestAPIUAT/RIT/";

    private static RestApi REST_CLIENT,REST_CLIENT1,REST_CLIENT_BASE;
    private static Retrofit restAdapter;
    private static Retrofit restAdapter1;
    private static Retrofit restAdapter_base;


    private static MediaType MEDIA_TYPE_TEXT = MediaType.parse("multipart/form-data");
    private static Gson gson;

    //  public static final String base_url = "https://ipapi.co/";
    static {
        setupRestClient();
        setupRestClient1();
        setupRestClientBase();
    }

    private static void setupRestClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // loggingInterceptor.redactHeader();
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
                .baseUrl(RitpayDomestic_live_base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }//where u added header where we have to add

    private static void setupRestClient1() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // loggingInterceptor.redactHeader();
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000,TimeUnit.SECONDS);
//        builder.connectTimeout(300, TimeUnit.SECONDS);
//        builder.readTimeout(80, TimeUnit.SECONDS);
//        builder.writeTimeout(90, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().setLenient().create();
        restAdapter1 = new Retrofit.Builder()
                .baseUrl(IndiaFirst_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    private static void setupRestClientBase() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // loggingInterceptor.redactHeader();
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000,TimeUnit.SECONDS);
//        builder.connectTimeout(300, TimeUnit.SECONDS);
//        builder.readTimeout(80, TimeUnit.SECONDS);
//        builder.writeTimeout(90, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().setLenient().create();
        restAdapter_base = new Retrofit.Builder()
                .baseUrl(RitpayDomestic_base_url)
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

    public static RestApi get1() {
        if (REST_CLIENT1 == null) {
            REST_CLIENT1 = restAdapter1.create(RestApi.class);
        }
        return REST_CLIENT1;
    }

    public static RestApi getBase() {
        if (REST_CLIENT_BASE == null) {
            REST_CLIENT_BASE = restAdapter_base.create(RestApi.class);
        }
        return REST_CLIENT_BASE;
    }


    @NonNull
    public static RequestBody makeGSONRequestBody(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return RequestBody.create(MEDIA_TYPE_TEXT, gson.toJson(jsonObject));
    }

}
