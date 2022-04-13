package angoothape.wallet.customerekyc;

import androidx.navigation.NavController;
import android.os.Bundle;
import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityCustomerEKYCMainBinding;
import angoothape.wallet.ekyc.viewmodels.EKYCViewModel;

public class CustomerEKYCMainActivity extends RitmanBaseActivity<ActivityCustomerEKYCMainBinding> {

    public NavController navController;
    public EKYCViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_e_k_y_c_main;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.createEkycAgentFragment) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}