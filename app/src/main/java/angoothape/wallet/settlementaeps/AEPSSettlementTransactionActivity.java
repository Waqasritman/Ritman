package angoothape.wallet.settlementaeps;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityAEPSSettlementTransactionBinding;
import angoothape.wallet.settlementaeps.viewmodels.AEPSSettlementViewModel;

public class AEPSSettlementTransactionActivity extends RitmanBaseActivity<ActivityAEPSSettlementTransactionBinding> {

    public NavController navController;
    public AEPSSettlementViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_a_e_p_s_settlement_transaction;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard_money_tranfser);
        viewModel = new ViewModelProvider(this).get(AEPSSettlementViewModel.class);
        binding.toolBar.titleTxt.setText(getString(R.string.aeps_settlement));
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.toolBarFinal
                .setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.AEPSBeneficiaryListFragment) {
            finish();
        } else {
            navController.navigateUp();
        }

    }
}