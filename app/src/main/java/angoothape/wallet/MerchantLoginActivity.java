package angoothape.wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
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

        if (!getSessionManager().getUserNumberRemember().isEmpty()) {
            binding.edtUserName.setText(getSessionManager()
                    .getUserNumberRemember());
        }


        binding.rememberMeCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!TextUtils.isEmpty(binding.edtUserName.getText().toString())) {
                    getSessionManager()
                            .userPhoneRemember(binding.edtUserName.getText().toString());
                }
                binding.rememberMeCheck.setChecked(true);
            } else {
                getSessionManager()
                        .userPhoneRemember("");
                binding.rememberMeCheck.setChecked(false);
            }
        });

        binding.btnSubmit.setOnClickListener(v ->
                validate());
        binding.tvForgotPassword.setOnClickListener(v ->
                startActivity(new Intent(MerchantLoginActivity.this,
                        ForgotPassword.class)));

        changeVisibility();
    }


    public void validate() {
        if (binding.edtUserName.getText().toString().equals("")) {
            onMessage(getString(R.string.enter_name_user_error));
        } else if (binding.edtPassword.getText().toString().equals("")) {
            onMessage(getString(R.string.enter_pin));
        } else {

            if (binding.rememberMeCheck.isChecked()) {
                if (!TextUtils.isEmpty(binding.edtUserName.getText().toString())) {
                    getSessionManager()
                            .userPhoneRemember(binding.edtUserName.getText().toString());
                }
            } else {
                getSessionManager()
                        .userPhoneRemember("");
            }
            getMerchantLogin();
        }
    }


    boolean isPasswordSeen = false;

    /**
     * change the input type of password filed on touch on eye icon
     */
    @SuppressLint("ClickableViewAccessibility")
    private void changeVisibility() {

        binding.passwordSeen.setOnClickListener(v -> {
            if (isPasswordSeen) {
                isPasswordSeen = false;
                binding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                binding.passwordSeen.setImageDrawable(getResources().getDrawable(R.drawable.eye));
            } else {
                isPasswordSeen = true;
                binding.edtPassword.setTransformationMethod(null);
                binding.passwordSeen.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
            }
            binding.edtPassword.setSelection(binding.edtPassword.getText().length());
        });
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
                        checkVerifiedEKYC();
                    } else if (response.body().responseCode.equals(504)) {
                        Intent intent = new Intent(MerchantLoginActivity.this, VerifyOtpActivity.class);
                        intent.putExtra("userName", binding.edtUserName.getText().toString());
                        startActivity(intent);

                    } else {
                        Utils.hideCustomProgressDialog();
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


    void checkVerifiedEKYC() {
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

                        startActivity(new Intent(MerchantLoginActivity.this,
                                NewDashboardActivity.class));

                    } else if (response.body().responseCode.equals(100)) {
                        sessionManager.setIsVerified("false");

                        startActivity(new Intent(MerchantLoginActivity.this,
                                NewDashboardActivity.class));

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