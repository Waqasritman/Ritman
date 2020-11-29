package totipay.wallet;


import android.os.Bundle;

import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityReferToFriendBinding;

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