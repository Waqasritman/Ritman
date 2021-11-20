package angoothape.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyEKYCRequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityMerchantLoginBinding;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.generic_response.SimpleResponse;
import angoothape.wallet.home_module.NewDashboardActivity;
import angoothape.wallet.utils.Utils;

public class MerchantLoginActivity extends RitmanBaseActivity<ActivityMerchantLoginBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_login;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.btnSubmit.setOnClickListener(v ->
                validate());
        binding.tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(MerchantLoginActivity.this,
                        ForgotPassword.class)));
    }


    public void validate() {
        if (binding.edtUserName.getText().toString().equals("")) {
            onMessage(getString(R.string.enter_name_user_error));
        } else if (binding.edtPassword.getText().toString().equals("")) {
            onMessage(getString(R.string.enter_pin));
        } else {
            getMerchantLogin();
        }
    }

    public void getMerchantLogin() {
        Utils.showCustomProgressDialog(this, false);
        Call<SimpleResponse> call = RestClient.get().createMerchant(new SimpleRequest(), binding.edtUserName.getText().toString()
                , binding.edtPassword.getText().toString());
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    sessionManager
                            .merchantName(binding.edtUserName.getText().toString());

                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        checkVerifiedEKYC(response.body().responseCode);
                    } else if (response.body().responseCode.equals(504)) {
                        checkVerifiedEKYC(response.body().responseCode);
                    } else {
                        Utils.hideCustomProgressDialog();
                        onMessage(response.body().description);
                    }


//
//
//                    if(response.body().responseCode.equals(101)) {
//                        sessionManager
//                                .merchantName(binding.edtUserName.getText().toString());
//                        checkVerifiedEKYC(response.body().responseCode);
//                    }
//                    else {
//                        Utils.hideCustomProgressDialog();
//                        onMessage(response.body().description);
//                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Utils.hideCustomProgressDialog();
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });


    }


    void checkVerifiedEKYC(Integer responseCode) {
        String gKey = KeyHelper.getKey(binding.edtUserName.getText().toString()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(binding.edtUserName.getText().toString())).trim();

        VerifyEKYCRequest loginRequest = new VerifyEKYCRequest();

        String body = RestClient.makeGSONString(loginRequest);
        Log.e("vv", body);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Call<AEResponse> call = RestClient.getEKYC().verifyEKYC(RestClient.makeGSONRequestBody(request),
                KeyHelper.getKey(binding.edtUserName.getText().toString()).trim()
                , KeyHelper.getSKey(KeyHelper
                        .getKey(binding.edtUserName.getText().toString())).trim());
        call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call, Response<AEResponse> response) {
                Utils.hideCustomProgressDialog();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        String bodyy = AESHelper.decrypt(response.body().data.body
                                , gKey);
                        Log.e("vv", bodyy.trim());
                        sessionManager.setIsVerified("true");
                        if (responseCode.equals(101)) {
                            startActivity(new Intent(MerchantLoginActivity.this,
                                    NewDashboardActivity.class));
                        } else if (responseCode.equals(504)) {
                            startActivity(new
                                    Intent(MerchantLoginActivity.this,
                                    VerifyOtpActivity.class));
                        } else {
                            onMessage(response.body().description);
                        }
                    } else if (response.body().responseCode.equals(100)) {
                        sessionManager.setIsVerified("false");
                        if (responseCode.equals(101)) {
                            startActivity(new Intent(MerchantLoginActivity.this,
                                    NewDashboardActivity.class));
                        } else if (responseCode.equals(504)) {
                            startActivity(new
                                    Intent(MerchantLoginActivity.this,
                                    VerifyOtpActivity.class));
                        } else {
                            onMessage(response.body().description);
                        }
                    } else {
                        onMessage(response.body().description);
                    }
                }
            }

            @Override
            public void onFailure(Call<AEResponse> call, Throwable t) {
                Utils.hideCustomProgressDialog();
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void showProgress() {
        super.showProgress();
    }


    @Override
    public void hideProgress() {
        super.hideProgress();
    }
}