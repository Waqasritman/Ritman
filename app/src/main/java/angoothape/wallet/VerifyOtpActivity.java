package angoothape.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_otp;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.btnVerifyOtp.setOnClickListener(v -> validate());
    }


    public void validate() {
        if (binding.edtOtp.getText().toString().equals("")) {
            onMessage("Please Enter your OTP");
        } else {
            getOtp();
        }
    }

    public void getOtp() {
        OtpRequest otpRequest = new OtpRequest();
        otpRequest.otp = binding.edtOtp.getText().toString();
        Call<SimpleResponse> call = RestClient.get().createotp(otpRequest, sessionManager.getMerchantName());
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        startActivity(new Intent(VerifyOtpActivity.this,
                                NewDashboardActivity.class));
                    }


                    else {
                        onMessage(response.body().description);
                       // binding.edtOtp.getText().clear();

                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}