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

    public static native String RitpayDomesticbaseurl();

    public static native String RitpayDomesticlivebaseurl();
    public static native String RitpayRestlivebaseurl();
    public static native String IndiaFirsturl();
    public static native String EKYC();

    public static final String RitpayDomestic_base_url = RitpayDomesticbaseurl();
    public static final String RitpayDomestic_live_base_url = RitpayDomesticlivebaseurl();
    public static final String IndiaFirst_url = IndiaFirsturl();

    private static RestApi REST_CLIENT, REST_CLIENT1, REST_CLIENT_BASE, REST_EKYC;
    private static Retrofit restAdapter;
    private static Retrofit restAdapter1;
    private static Retrofit restAdapter_base;
    private static Retrofit restAdapterEKYC;
    private static Retrofit restAdapterRest;


    private static MediaType MEDIA_TYPE_TEXT = MediaType.parse("multipart/form-data");
    private static Gson gson;

    static {
        setupRestClient();
        setupRestClient1();
        setupRestClientBase();
        setupClientEKYC();
        setupClientREST();
    }


    private static void setupClientREST() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000, TimeUnit.SECONDS);


        Gson gson = new GsonBuilder().setLenient().create();
        restAdapterRest = new Retrofit.Builder()
                .baseUrl(RitpayRestlivebaseurl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
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
                .baseUrl(RitpayDomestic_live_base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    private static void setupRestClient1() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);

        builder.connectTimeout(5000, TimeUnit.SECONDS);
        builder.readTimeout(5000, TimeUnit.SECONDS);
        builder.writeTimeout(5000, TimeUnit.SECONDS);
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
        builder.writeTimeout(5000, TimeUnit.SECONDS);
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

    public static RestApi getEKYC() {
        if (REST_EKYC == null) {
            REST_EKYC = restAdapterEKYC.create(RestApi.class);
        }
        return REST_EKYC;
    }


    public static RestApi getREST() {
        REST_EKYC = restAdapterRest.create(RestApi.class);
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
