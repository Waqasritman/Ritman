package tootipay.wallet;

import android.os.Bundle;

import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivityLoyalityPointsBinding;

public class LoyalityPointsActivity extends TootiBaseActivity<ActivityLoyalityPointsBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_loyality_points;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {

        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}