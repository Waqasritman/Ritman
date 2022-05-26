package angoothape.wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.LoginRequest;
import angoothape.wallet.di.JSONdi.restRequest.VerifyEKYCRequest;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.utils.CryptoHelper;
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

    String token = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_merchant_login;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {

        if (!getSessionManager().getUserNumberRemember().isEmpty()) {
            binding.edtUserName.setText(getSessionManager()
                    .getUserNumberRemember());
            binding.rememberMeCheck.setChecked(true);
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
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token = task.getResult();
                    Log.e("onComplete: ", token);
                });

//
//        try {
//            String encrypt = CryptoHelper.encrypt("waqas");
//            Log.e("ENCRYPT: ", encrypt);
//            Log.e("DECRYPT: ", CryptoHelper.decrypt(encrypt));
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }

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

//    public void getMerchantLogin() {
//        Utils.showCustomProgressDialog(this, false);
//
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.UserPassword = binding.edtPassword.getText().toString();
//        loginRequest.Device_Token = token;
//        String gKey = KeyHelper.getKey(binding.edtUserName.getText().toString()).trim() + KeyHelper.getSKey(KeyHelper
//                .getKey(binding.edtUserName.getText().toString())).trim();
//
//        String body = RestClient.makeGSONString(loginRequest);
//        Log.e("getMerchantLogin: ", body);
//        AERequest aeRequest = new AERequest();
//        aeRequest.body = AESHelper.encrypt(body, gKey.trim());
//
//        Call<AEResponse> call = RestClient.getEKYC().loginTokenUser(RestClient.makeGSONRequestBody(aeRequest),
//                KeyHelper.getKey(binding.edtUserName.getText().toString()).trim(), KeyHelper.getSKey(KeyHelper
//                        .getKey(binding.edtUserName.getText().toString())).trim());
//        call.enqueue(new Callback<AEResponse>() {
//            @Override
//            public void onResponse(@NotNull Call<AEResponse> call, @NotNull Response<AEResponse> response) {
//                if (response.isSuccessful()) {
//                    sessionManager.merchantName(binding.edtUserName.getText().toString());
//                    assert response.body() != null;
//                    if (response.body().responseCode.equals(101)) {
//                        checkVerifiedEKYC();
//                    } else if (response.body().responseCode.equals(504)) {
//                        Utils.hideCustomProgressDialog();
//                        Intent intent = new Intent(MerchantLoginActivity.this,
//                                VerifyOtpActivity.class);
//                        intent.putExtra("userName", binding.edtUserName.getText().toString());
//                        startActivity(intent);
//                 //     checkVerifiedEKYC();
//                    } else {
//                        Utils.hideCustomProgressDialog();
//                        onError(response.body().description);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<AEResponse> call, @NotNull Throwable t) {
//                Utils.hideCustomProgressDialog();
//                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
//            }
//        });
//    }

    public void getMerchantLogin() {
        Utils.showCustomProgressDialog(this, false);
        Call<SimpleResponse> call = RestClient.get().loginUser(new SimpleRequest(),
                binding.edtUserName.getText().toString()
                , binding.edtPassword.getText().toString());
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(@NotNull Call<SimpleResponse> call, @NotNull Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    sessionManager
                            .merchantName(binding.edtUserName.getText().toString());

                    assert response.body() != null;
                    if (response.body().responseCode.equals(101)) {
                        // checkVerifiedEKYC();

                        sessionManager.setIsVerified("true");
                        startActivity(new Intent(MerchantLoginActivity.this,
                                NewDashboardActivity.class));

                    } else if (response.body().responseCode.equals(504)) {
//                        Intent intent = new Intent(MerchantLoginActivity.this,
//                                VerifyOtpActivity.class);
//                        intent.putExtra("userName", binding.edtUserName.getText().toString());
//                        startActivity(intent);

                        sessionManager.setIsVerified("true");
                        startActivity(new Intent(MerchantLoginActivity.this,
                                NewDashboardActivity.class));
                    } else {
                        Utils.hideCustomProgressDialog();
                        onError(response.body().description);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<SimpleResponse> call, @NotNull Throwable t) {
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
                        sessionManager.setIsVerified("true");
                        startActivity(new Intent(MerchantLoginActivity.this,
                                NewDashboardActivity.class));
                        finish();
                    } else if (response.body().responseCode.equals(100)) {
                        sessionManager.setIsVerified("false");
                        startActivity(new Intent(MerchantLoginActivity.this,
                                NewDashboardActivity.class));
                        finish();

                    } else {
                        onError(response.body().description);
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