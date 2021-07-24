package ritman.wallet;

import android.os.Bundle;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityForgotPasswordBinding;

public class ForgotPassword extends RitmanBaseActivity<ActivityForgotPasswordBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.titleTxt.setText("Forgot Password");
        binding.toolBar.titleTxt.setTextColor(getResources().getColor(R.color.black));
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}