package ritman.wallet.MoneyTransferModuleV;


import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.os.Bundle;
import android.util.Log;

import ritman.wallet.MoneyTransferModuleV.viewmodels.BankTransferViewModel;
import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import ritman.wallet.databinding.ActivityMoneyTransferMainLayoutBinding;

public class MoneyTransferMainLayout extends RitmanBaseActivity<ActivityMoneyTransferMainLayoutBinding> {

    public NavController navController;
    public BankTransferViewModel bankTransferViewModel;
    public RegisterBeneficiaryViewModel viewModel;
    @Override
    public int getLayoutId() {
        return R.layout.activity_money_transfer_main_layout;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard_money_tranfser);
        bankTransferViewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        viewModel = new ViewModelProvider(this).get(RegisterBeneficiaryViewModel.class);
        //  setSupportActionBar(binding.toolBar.toolBarFinal);
        binding.toolBar.titleTxt.setText(getString(R.string.app_name));
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
        Log.e("onBackPressed: ", "");
        if (navController.getCurrentDestination().getId() == R.id.selectMoneyTransferTypeActivity) {
            finish();
        } else {
            navController.navigateUp();
        }

    }
}