package ritman.wallet.my_bank_cards;

import android.os.Bundle;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityAvalableBanksBinding;

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