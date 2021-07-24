package ritman.wallet.menumodules;

import android.content.Intent;
import android.os.Bundle;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityNewAboutBinding;

public class SettingsActivity extends RitmanBaseActivity<ActivityNewAboutBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_new_about;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });


        binding.aboutusLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, AboutActivity.class));
        });

        binding.termsAndCondLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsActivity.class));
        });

        binding.privacyPolicyLayout.setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyActivity.class));
        });
    }
}