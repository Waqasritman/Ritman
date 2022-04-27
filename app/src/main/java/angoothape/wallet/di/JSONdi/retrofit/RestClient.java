package angoothape.wallet.di.JSONdi.retrofit;

import android.content.Context;

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

public class RestClient {


    public static Context context;

    public RestClient(Context context) {
        this.context = context;
    }

    static {
        System.loadLibrary("native-lib");
    }

    public static native String EKYC();
    public static native String RitpayDomesticlivebaseurl();

    private static RestApi REST_CLIENT, REST_EKYC;
    private static Retrofit restAdapter;
    private static Retrofit restAdapterEKYC;

    private static MediaType MEDIA_TYPE_TEXT = MediaType.parse("multipart/form-data");
    private static Gson gson;

    static {
        setupRestClient();
        setupClientEKYC();
    }


    private static void setupClientEKYC() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000, TimeUnit.SECONDS);


        Gson gson = new GsonBuilder().setLenient().create();
        restAdapterEKYC = new Retrofit.Builder()
                .baseUrl(EKYC())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    private static void setupRestClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000, TimeUnit.SECONDS);


        Gson gson = new GsonBuilder().setLenient().create();
        restAdapter = new Retrofit.Builder()
                .baseUrl(RitpayDomesticlivebaseurl())
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


    public static RestApi getEKYC() {
        if (REST_EKYC == null) {
            REST_EKYC = restAdapterEKYC.create(RestApi.class);
        }
        return REST_EKYC;
    }


    @NonNull
    public static RequestBody makeGSONRequestBody(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return RequestBody.create(MEDIA_TYPE_TEXT, gson.toJson(jsonObject));
    }


    @NonNull
    public static String makeGSONString(Object jsonObject) {
        if (gson == null) {
            gson = new Gson();
        }
        return gson.toJson(jsonObject);
    }

}
