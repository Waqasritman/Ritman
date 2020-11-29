package totipay.wallet.KYC;


import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityKycfirstBinding;


public class KYCMainActivity extends TootiBaseActivity<ActivityKycfirstBinding> {

    private NavController navController;
    public KYCViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.activity_kycfirst;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        viewModel = new ViewModelProvider(this).get(KYCViewModel.class);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());


        binding.toolBar.crossBtn.setOnClickListener(v->{
            onClose();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.takeKYCDataFragment
                || navController.getCurrentDestination().getId() == R.id.finalKYCFragment) {
            finish();
        } else {
            navController.navigateUp();
        }
    }

}
