package ritman.wallet.Mobile_Top_Up;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import ritman.wallet.Mobile_Top_Up.viewmodels.MobileTopUpViewModel;
import ritman.wallet.R;
import ritman.wallet.base.RitmanBaseActivity;
import ritman.wallet.databinding.ActivityMobileTopUpMainBinding;
import ritman.wallet.di.XMLdi.RequestHelper.GetWRPrepaidPlansRequest;
import ritman.wallet.di.XMLdi.RequestHelper.WRBillerPlansRequest;
import ritman.wallet.di.XMLdi.RequestHelper.WRPayBillRequest;
import ritman.wallet.di.XMLdi.RequestHelper.WRPrepaidRechargeRequest;

public class MobileTopUpMainActivity extends RitmanBaseActivity<ActivityMobileTopUpMainBinding> {

    NavController navController;
    public MobileTopUpViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mobile_top_up_main;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        viewModel = new ViewModelProvider(this).get(MobileTopUpViewModel.class);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.titleTxt.setText(getString(R.string.mobile_top_up));
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


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
        if (navController.getCurrentDestination().getId() == R.id.mobileTopUpFirstActivity
                || navController.getCurrentDestination().getId() == R.id.mobileTopupStatusFragment) {
            finish();
        } else {
            navController.navigateUp();
        }

    }
}