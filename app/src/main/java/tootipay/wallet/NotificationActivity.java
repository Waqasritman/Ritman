package tootipay.wallet;

import android.os.Bundle;

import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivityNotificationBinding;

public class NotificationActivity extends TootiBaseActivity<ActivityNotificationBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {

        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}