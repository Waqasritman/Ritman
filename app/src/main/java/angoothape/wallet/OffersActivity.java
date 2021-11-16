package angoothape.wallet;

import android.os.Bundle;

import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityOffersBinding;

public class OffersActivity extends RitmanBaseActivity<ActivityOffersBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_offers;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}