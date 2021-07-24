package ritman.wallet.beneficiary_list_module;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityBeneficiaryListBinding;

public class BeneficiaryListActivity extends RitmanBaseActivity<ActivityBeneficiaryListBinding> {


    NavController navController;

    @Override
    public int getLayoutId() {
        return R.layout.activity_beneficiary_list;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);


        binding.toolBar.backBtn.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.beneficiaryListFragment) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}