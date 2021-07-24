package ritman.wallet.menumodules;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityAboutBinding;

public class AboutActivity extends RitmanBaseActivity<ActivityAboutBinding> {

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