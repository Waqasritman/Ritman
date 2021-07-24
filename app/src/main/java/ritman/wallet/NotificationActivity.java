package ritman.wallet;

import android.os.Bundle;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityNotificationBinding;

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