package angoothape.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyEKYCRequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityVerifyOtpBinding;
import angoothape.wallet.di.JSONdi.restRequest.OtpRequest;
import angoothape.wallet.di.generic_response.SimpleResponse;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.home_module.NewDashboardActivity;

public class VerifyOtpActivity extends RitmanBaseActivity<ActivityVerifyOtpBinding> {

    String userName = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_otp;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        userName = getIntent().getExtras().getString("userName", "");
        binding.toolBar.titleTxt.setText("Verify OTP");

        binding.btnVerifyOtp.setOnClickListener(v -> {
            validate();
        });


        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
        binding.toolBar.crossBtn.setVisibility(View.GONE);
    }


    public void validate() {
        if (binding.edtOtp.getText().toString().equals("")) {
            onMessage("Please Enter your OTP");
        } else if (binding.edtOtp.getText().toString().length() < 4) {
            onMessage("Please Enter your Four digit OTP");
        } else {
            getOtp();
        }
    }

    public void getOtp() {
        binding.btnVerifyOtp.setEnabled(false);
        showProgress();
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.otp = binding.edtOtp.getText().toString();
        Call<SimpleResponse> call = RestClient.get().createotp(otpRequest, sessionManager.getMerchantName());
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        checkVerifiedEKYC();
                    } else {
                        binding.btnVerifyOtp.setEnabled(true);
                        hideProgress();
                        onError(response.body().description);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                binding.btnVerifyOtp.setEnabled(true);
                hideProgress();
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    void checkVerifiedEKYC() {
        String gKey = KeyHelper.getKey(userName).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(userName).trim());

        VerifyEKYCRequest loginRequest = new VerifyEKYCRequest();

        String body = RestClient.makeGSONString(loginRequest);
        Log.e("vv", body);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Call<AEResponse> call = RestClient.getEKYC().verifyEKYC(RestClient.makeGSONRequestBody(request),
                KeyHelper.getKey(userName).trim()
                , KeyHelper.getSKey(KeyHelper
                        .getKey(userName)).trim());
        call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call, Response<AEResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        String bodyy = AESHelper.decrypt(response.body().data.body
                                , gKey);
                        Log.e("vv", bodyy.trim());
                        sessionManager.setIsVerified("true");
                        startActivity(new Intent(VerifyOtpActivity.this,
                                NewDashboardActivity.class));
                    } else if (response.body().responseCode.equals(100)) {
                        sessionManager.setIsVerified("false");
                        startActivity(new Intent(VerifyOtpActivity.this,
                                NewDashboardActivity.class));
                    } else {
                        binding.btnVerifyOtp.setEnabled(true);
                        Utils.hideCustomProgressDialog();
                        if (response.body().data != null) {
                            String bodyy = AESHelper.decrypt(response.body().data.body
                                    , gKey);
                            if (!body.isEmpty()) {
                                onError(bodyy);
                            } else {
                                onError(response.body().description);
                            }
                        } else {
                            onError(response.body().description);
                        }
                    }
                }
                hideProgress();
            }

            @Override
            public void onFailure(Call<AEResponse> call, Throwable t) {
                hideProgress();
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void showProgress() {
        super.showProgress();
        binding.btnVerifyOtp.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideProgress() {
        super.hideProgress();
        binding.btnVerifyOtp.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
}