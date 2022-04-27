package angoothape.wallet;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.SplashLoginBinding;
import angoothape.wallet.di.JSONdi.restRequest.OsVersionRequest;
import angoothape.wallet.di.JSONdi.restResponse.CountryIpResponse;
import angoothape.wallet.di.JSONdi.restResponse.OsVersionResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.JSONdi.retrofit.TrackRestClient;

import angoothape.wallet.utils.Utils;

public class NewSplash extends RitmanBaseActivity<SplashLoginBinding> {

    private Handler mHandler;
    String version = "";

    @Override
    public int getLayoutId() {
        return R.layout.splash_login;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                getOsVersion();
            }
        }, 7000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startVideo();
    }


    void startVideo() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logoo);
        binding.videoView.setVideoURI(uri);
        binding.imgLogo.setVisibility(View.GONE);
        binding.videoView.setVisibility(View.VISIBLE);
        binding.videoView.setOnPreparedListener(mp -> {
            binding.videoView.start();
        });
        binding.videoView.setOnCompletionListener(mp -> {
            // Toast.makeText(getApplicationContext(), "Video completed", Toast.LENGTH_LONG).show();
        });
    }

    public void getOsVersion() {

        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;

        try {
            info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        Utils.showCustomProgressDialog(this, false);
        OsVersionRequest request = new OsVersionRequest();
        request.os_type = "Android";
        Call<OsVersionResponse> call = RestClient.get().getOsVersion(request);
        call.enqueue(new Callback<OsVersionResponse>() {
            @Override
            public void onResponse(Call<OsVersionResponse> call, Response<OsVersionResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        if (response.body().data.Force_Update && !response.body().data.Andriod_Version.equalsIgnoreCase(version)) {
                            updateAvailable();
                        } else {
                            getCountry();
                        }
                    } else {
                        onError(response.body().description);
                    }
                    Utils.hideCustomProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<OsVersionResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }


    public void updateAvailable() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.update_available))
                .setMessage(getString(R.string.update_available_msg))
                .setPositiveButton(R.string.update, (dialog, which) -> {
                    // Continue with delete operation
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + appPackageName)));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, (dialog, which) -> finish())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    void getCountry() {

        Call<CountryIpResponse> call = TrackRestClient.get()
                .getIP(TrackRestClient.baseKEY());

        call.enqueue(new Callback<CountryIpResponse>() {
            @Override
            public void onResponse(Call<CountryIpResponse> call, retrofit2.Response<CountryIpResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    sessionManager.setIpAddress(response.body().ip);
                    sessionManager.IpCountryName(response.body().country_name);
                } else {
                    //   onMessage(getString(R.string.contact_to_admin));
                    sessionManager.setIpAddress("");
                    sessionManager.IpCountryName("");
                }
                goToNext();
            }

            @Override
            public void onFailure(Call<CountryIpResponse> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
                //     onMessage(getString(R.string.contact_to_admin));
                sessionManager.setIpAddress("");
                sessionManager.IpCountryName("");
                goToNext();
            }
        });
    }


    void goToNext() {
        startActivity(new Intent(getApplicationContext(), MerchantLoginActivity.class));
        finish();
    }


}
