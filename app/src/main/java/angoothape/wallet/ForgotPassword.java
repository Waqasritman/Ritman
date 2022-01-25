package angoothape.wallet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityForgotPasswordBinding;
import angoothape.wallet.di.AESHelper;
import angoothape.wallet.di.JSONdi.restRequest.AERequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequest;
import angoothape.wallet.di.JSONdi.restRequest.SimpleRequestCred;
import angoothape.wallet.di.JSONdi.restResponse.AEResponse;
import angoothape.wallet.di.JSONdi.retrofit.KeyHelper;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.dialogs.SingleButtonMessageDialog;
import angoothape.wallet.interfaces.OnDecisionMade;
import angoothape.wallet.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends RitmanBaseActivity<ActivityForgotPasswordBinding> implements OnDecisionMade {

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.titleTxt.setText(getString(R.string.app_name));
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());

        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);


        binding.toolBar.crossBtn.setVisibility(View.GONE);

        binding.btnSubmit.setOnClickListener(v -> {
            if(binding.edtUserName.getText().toString().isEmpty()) {
                onMessage("Enter user name");
            } else {
                getMerchantLogin();
            }
        });
    }


    public void getMerchantLogin() {
        Utils.showCustomProgressDialog(this, false);

        String gKey = KeyHelper.getKey(binding.edtUserName.getText().toString()).trim() + KeyHelper.getSKey(KeyHelper
                .getKey(binding.edtUserName.getText().toString())).trim();

        SimpleRequestCred loginRequest = new SimpleRequestCred();
        String body = RestClient.makeGSONString(loginRequest);
        Log.e("vv", body);

        AERequest request = new AERequest();
        request.body = AESHelper.encrypt(body.trim(), gKey.trim());
        Call<AEResponse> call = RestClient.getEKYC().forgotPassword(RestClient.makeGSONRequestBody(request),
                KeyHelper.getKey(binding.edtUserName.getText().toString()).trim()
                , KeyHelper.getSKey(KeyHelper
                        .getKey(binding.edtUserName.getText().toString())).trim());
        call.enqueue(new Callback<AEResponse>() {
            @Override
            public void onResponse(Call<AEResponse> call, Response<AEResponse> response) {
                Utils.hideCustomProgressDialog();
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().responseCode.equals(101)) {
                        String bodyy = AESHelper.decrypt(response.body().data.body
                                , gKey);
                        showMessage(response.body().description , bodyy);
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


    void showMessage(String title , String message) {
        SingleButtonMessageDialog dialog = new
                SingleButtonMessageDialog(title
                , message, this,
                false);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        dialog.show(transaction, "");
    }

    @Override
    public void onProceed() {
        finish();
    }

    @Override
    public void onCancel(boolean goBack) {
        finish();
    }
}