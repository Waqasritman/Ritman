package angoothape.wallet;


import android.os.Bundle;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityReferToFriendBinding;

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