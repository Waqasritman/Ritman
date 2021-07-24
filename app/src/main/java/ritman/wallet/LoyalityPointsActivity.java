package ritman.wallet;

import android.os.Bundle;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityLoyalityPointsBinding;

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