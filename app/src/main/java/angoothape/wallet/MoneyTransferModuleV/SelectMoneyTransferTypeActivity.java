package angoothape.wallet.MoneyTransferModuleV;

import android.os.Bundle;

import androidx.navigation.Navigation;


import angoothape.wallet.R;
import angoothape.wallet.databinding.ActivitySelectTypeBinding;
import angoothape.wallet.fragments.BaseFragment;
import angoothape.wallet.utils.BeneficiarySelector;

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
