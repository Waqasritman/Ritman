package totipay.wallet;

import android.os.Bundle;

import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityNotificationBinding;

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