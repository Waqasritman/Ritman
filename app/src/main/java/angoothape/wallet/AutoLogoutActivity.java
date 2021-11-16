package angoothape.wallet;

import android.content.Intent;
import android.os.Bundle;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityAutoLogoutBinding;

public class AutoLogoutActivity extends RitmanBaseActivity<ActivityAutoLogoutBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_auto_logout;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.closeTalfie.setOnClickListener(v -> {
            finish();
        });

        binding.login.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewSplash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}