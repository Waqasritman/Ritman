package totipay.wallet.menumodules;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityChangePinBinding;
import totipay.wallet.di.RequestHelper.ChangePin;
import totipay.wallet.di.apicaller.ChangePinTask;
import totipay.wallet.interfaces.OnSuccessMessage;
import totipay.wallet.utils.Constants;
import totipay.wallet.utils.IsNetworkConnection;


public class ChangePinActivity extends TootiBaseActivity<ActivityChangePinBinding>
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
                    ChangePin changePin = new ChangePin();
                    changePin.oldPin = binding.oldPin.getText().toString();
                    changePin.newPin = binding.newPin.getText().toString();
                    changePin.confirmPin = binding.confirmPin.getText().toString();
                    changePin.customerNo = sessionManager.getCustomerNo();
                    changePin.languageId = sessionManager.getlanguageselection();
                    ChangePinTask task = new ChangePinTask(this, this);
                    task.execute(changePin);
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