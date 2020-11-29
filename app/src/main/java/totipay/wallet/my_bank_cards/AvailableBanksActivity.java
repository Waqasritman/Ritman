package totipay.wallet.my_bank_cards;

import android.os.Bundle;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityAvalableBanksBinding;

public class AvailableBanksActivity extends TootiBaseActivity<ActivityAvalableBanksBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_avalable_banks;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        binding.toolBar.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}