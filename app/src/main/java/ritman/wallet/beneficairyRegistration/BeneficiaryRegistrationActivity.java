package ritman.wallet.beneficairyRegistration;


import android.os.Bundle;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.beneficairyRegistration.viewmodel.RegisterBeneficiaryViewModel;
import ritman.wallet.databinding.ActivityBeneficiaryRegistrationBinding;
import ritman.wallet.dialogs.CloseDialog;

public class BeneficiaryRegistrationActivity extends RitmanBaseActivity<ActivityBeneficiaryRegistrationBinding> {


    public NavController navController;
    public RegisterBeneficiaryViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_beneficiary_registration;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard_money_tranfser);
        viewModel = new ViewModelProvider(this).get(RegisterBeneficiaryViewModel.class);
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


    /**
     * will handle navigation between fragments
     * can say its backpressed for navigation
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.selectBeneficiaryFragment) {
            finish();
        } else {
            navController.navigateUp();
        }

    }
}