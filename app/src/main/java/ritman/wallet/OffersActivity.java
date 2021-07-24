package ritman.wallet;

import android.os.Bundle;

import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityOffersBinding;

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