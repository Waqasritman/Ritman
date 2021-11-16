package angoothape.wallet.my_bank_cards;

import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityAvalableBanksBinding;

public class AvailableBanksActivity extends RitmanBaseActivity<ActivityAvalableBanksBinding> {

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