package totipay.wallet.mywalletmoduleV.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;

import totipay.wallet.R;
import totipay.wallet.databinding.ActivityAddedMoneyWalletBinding;
import totipay.wallet.fragments.BaseFragment;

public class AddedMoneyWalletActivity extends BaseFragment<ActivityAddedMoneyWalletBinding> {


    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.backButton.setOnClickListener(view -> Navigation.findNavController(view).navigateUp());
        binding.viewWallet.setOnClickListener(view -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_added_money_wallet;
    }

}