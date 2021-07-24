package ritman.wallet;


import android.os.Bundle;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityReferToFriendBinding;

public class ReferToFriendActivity extends RitmanBaseActivity<ActivityReferToFriendBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_refer_to_friend;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}