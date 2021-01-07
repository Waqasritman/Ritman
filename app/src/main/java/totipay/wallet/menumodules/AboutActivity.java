package totipay.wallet.menumodules;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityAboutBinding;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AboutActivity extends TootiBaseActivity<ActivityAboutBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void initUi(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.toolBar.titleTxt.setText(getString(R.string.faq));
            binding.toolBar.titleTxt.setTextColor(Color.parseColor("#000000"));
        }

        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}