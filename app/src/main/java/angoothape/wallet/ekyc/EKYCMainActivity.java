package angoothape.wallet.ekyc;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.databinding.ActivityEKYCMainBinding;
import angoothape.wallet.ekyc.viewmodels.EKYCViewModel;

public class EKYCMainActivity extends RitmanBaseActivity<ActivityEKYCMainBinding> {

    public NavController navController;

    public EKYCViewModel viewModel;

    public boolean isCustomer = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_e_k_y_c_main;
    }

    @Override
    protected void initUi(Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(EKYCViewModel.class);
        isCustomer = getIntent().getExtras().getBoolean("is_customer");

        navController = Navigation.findNavController(this, R.id.ekycnav);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.posRed));
        binding.toolBar.titleTxt.setText("EKYC");

        binding.toolBar.backBtn.setOnClickListener((View.OnClickListener) v -> finish());
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
        if (navController.getCurrentDestination().getId() == R.id.createEkycAgentFragment) {
            finish();
        } else {
            navController.navigateUp();

        }
    }
}