package angoothape.wallet;

import android.os.Bundle;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityNotificationBinding;

public class NotificationActivity extends RitmanBaseActivity<ActivityNotificationBinding> {

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