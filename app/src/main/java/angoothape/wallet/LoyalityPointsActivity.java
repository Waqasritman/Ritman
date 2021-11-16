package angoothape.wallet;

import android.os.Bundle;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityLoyalityPointsBinding;

public class LoyalityPointsActivity extends RitmanBaseActivity<ActivityLoyalityPointsBinding> {

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