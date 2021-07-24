package ritman.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityMerchantLoginBinding;
import ritman.wallet.di.JSONdi.restRequest.SimpleRequest;
import ritman.wallet.di.JSONdi.retrofit.RestClient;
import ritman.wallet.di.XMLdi.RequestHelper.LoginRequest;
import ritman.wallet.di.generic_response.SimpleResponse;
import ritman.wallet.home_module.NewDashboardActivity;
import ritman.wallet.utils.Utils;

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
                Utils.hideCustomProgressDialog();
                if (response.isSuccessful()) {
                    sessionManager
                            .merchantName(binding.edtUserName.getText().toString());

                    if (response.body().responseCode.equals(101)) {
                        startActivity(new Intent(MerchantLoginActivity.this,
                                NewDashboardActivity.class));

                    } else if (response.body()
                            .responseCode.equals(504)) {
                        startActivity(new
                                Intent(MerchantLoginActivity.this,
                                VerifyOtpActivity.class));
                    } else {
                        onMessage(response.body().description);
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
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