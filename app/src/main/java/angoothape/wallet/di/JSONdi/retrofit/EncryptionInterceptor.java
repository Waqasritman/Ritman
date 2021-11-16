package angoothape.wallet.di.JSONdi.retrofit;


import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class EncryptionInterceptor  implements Interceptor {

    private String token;
    Context context ;

    public EncryptionInterceptor(Context context) {
        this.context = context;
    }


    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        if (!token.isEmpty()) {
            // if token is empty, don't bother adding authorization header
            // else attach token
            Request newRequest = request.newBuilder()
                    .addHeader("Authorization", String.format("Bearer %s", token))
                    .build();
            return chain.proceed(newRequest);
        }
        return chain.proceed(request);
    }
}