package tootipay.wallet;


import android.os.Bundle;

import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivityReferToFriendBinding;

public class ReferToFriendActivity extends TootiBaseActivity<ActivityReferToFriendBinding> {

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