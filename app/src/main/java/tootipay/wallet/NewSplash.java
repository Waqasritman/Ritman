package tootipay.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.SplashLoginBinding;
import tootipay.wallet.di.restResponse.CountryIpResponse;
import tootipay.wallet.di.retrofit.RestClient;
import tootipay.wallet.di.retrofit.TrackRestClient;
import tootipay.wallet.login_signup_module.MainActivityLoginSignUp;

public class NewSplash extends TootiBaseActivity<SplashLoginBinding> {


    //    code for language screen

    private Handler mHandler;

    @Override
    public int getLayoutId() {
        return R.layout.splash_login;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {

        Call<CountryIpResponse> call = TrackRestClient.get()
                .getIP("xqgm3PkZb4RTXmxMmikizvh4d0IDDjELyWW9VtLYETSANVG8H0");

        call.enqueue(new Callback<CountryIpResponse>() {
            @Override
            public void onResponse(Call<CountryIpResponse> call, retrofit2.Response<CountryIpResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    sessionManager.setIpAddress(response.body().ip);
                    sessionManager.IpCountryName(response.body().country_name);
                    goToNext();
                } else {
                    onMessage(getString(R.string.contact_to_admin));
                }
            }

            @Override
            public void onFailure(Call<CountryIpResponse> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage() );
                onMessage(getString(R.string.contact_to_admin));
            }
        });









    }


    void goToNext() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                startActivity(new Intent(getApplicationContext(), MainActivityLoginSignUp.class));
                /* startActivity(new Intent(getApplicationContext(),NewDashboardActivity.class));*/
                finish();
            }
        }, 2000);
    }


}
