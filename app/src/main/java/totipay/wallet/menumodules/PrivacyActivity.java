package totipay.wallet.menumodules;


import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityPrivacyBinding;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class PrivacyActivity extends TootiBaseActivity<ActivityPrivacyBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUi(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.termsText.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }


        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}