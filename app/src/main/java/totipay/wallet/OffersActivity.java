package totipay.wallet;

import android.os.Bundle;

import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityOffersBinding;

public class OffersActivity extends TootiBaseActivity<ActivityOffersBinding> {

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