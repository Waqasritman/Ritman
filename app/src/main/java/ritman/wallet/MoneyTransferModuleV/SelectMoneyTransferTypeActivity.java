package ritman.wallet.MoneyTransferModuleV;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;


import ritman.wallet.LoyalityPointsActivity;
import ritman.wallet.R;
import ritman.wallet.MoneyTransferModuleV.wallettransfer.local.WalletTransferFirstActivity;
import ritman.wallet.databinding.ActivitySelectTypeBinding;
import ritman.wallet.fragments.BaseFragment;
import ritman.wallet.utils.BeneficiarySelector;

public class SelectMoneyTransferTypeActivity extends BaseFragment<ActivitySelectTypeBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MoneyTransferMainLayout) getBaseActivity()).binding.toolBar.titleTxt
                .setText(getString(R.string.app_name));
    }

    @Override
    protected void setUp(Bundle savedInstanceState) {

        binding.bankTransferLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("transfer_type", BeneficiarySelector.BANK_TRANSFER);
            Navigation.findNavController(binding.getRoot()).navigate(R.id
                    .action_selectMoneyTransferTypeActivity_to_register_bene, bundle);

        });


        binding.cashTransfer.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("transfer_type", BeneficiarySelector.VERIFY_BENE);
            Navigation.findNavController(binding.getRoot()).navigate(R.id
                    .action_selectMoneyTransferTypeActivity_to_register_bene, bundle);
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_type;
    }



}
