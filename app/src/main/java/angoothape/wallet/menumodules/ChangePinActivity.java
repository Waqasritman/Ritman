package angoothape.wallet.menumodules;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;
import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityChangePinBinding;
import angoothape.wallet.di.JSONdi.retrofit.RestClient;
import angoothape.wallet.di.XMLdi.RequestHelper.ChangePin;
import angoothape.wallet.di.generic_response.SimpleResponse;
import angoothape.wallet.interfaces.OnSuccessMessage;
import angoothape.wallet.utils.Constants;
import angoothape.wallet.utils.IsNetworkConnection;
import angoothape.wallet.utils.Utils;


public class ChangePinActivity extends RitmanBaseActivity<ActivityChangePinBinding>
        implements OnSuccessMessage {


    public boolean isValidate() {
        if (TextUtils.isEmpty(binding.oldPin.getText().toString())) {
            onMessage(getString(R.string.enter_old_pin));
            return false;
        } else if (TextUtils.isEmpty(binding.newPin.getText().toString())) {
            onMessage(getString(R.string.enter_new_pin));
            return false;
        } else if (TextUtils.isEmpty(binding.confirmPin.getText().toString())) {
            onMessage(getString(R.string.enter_confirm_pin));
            return false;
        } else if (!TextUtils.equals(binding.newPin.getText().toString(), binding.confirmPin.getText().toString())) {
            onMessage(getString(R.string.pin_must_be_same));
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_pin;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.changePin.setOnClickListener(v -> {
            Constants.hideKeyboard(this);
            if (isValidate()) {
                if (IsNetworkConnection.checkNetworkConnection(this)) {
                    Utils.showCustomProgressDialog(this, false);
                    ChangePin changePin = new ChangePin();
                    changePin.OldPassword = binding.oldPin.getText().toString();
                    changePin.NewPassword = binding.newPin.getText().toString();
                    changePin.ConfirmPassword = binding.confirmPin.getText().toString();

                    Call<SimpleResponse> call = RestClient.get().changePassword(RestClient.makeGSONRequestBody(changePin)
                            , sessionManager.getMerchantName());
                    call.enqueue(new retrofit2.Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            Utils.hideCustomProgressDialog();
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                if (response.body().responseCode.equals(101)) {
                                    onSuccess("");
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
            }

        });


        binding.toolBarFinal.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onSuccess(String s) {
        onResponseMessage(getString(R.string.pin_changed_successfully));
        Handler mHandler;
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHandler.removeCallbacks(this);
                finish();
            }
        }, 400);
    }

    @Override
    public void onResponseMessage(String message) {
        onMessage(message);
    }
}