package totipay.wallet.Mobile_Top_Up;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import totipay.wallet.R;
import totipay.wallet.base.TootiBaseActivity;
import totipay.wallet.databinding.ActivityMobileTopUpMainBinding;
import totipay.wallet.di.RequestHelper.GetWRPrepaidPlansRequest;
import totipay.wallet.di.RequestHelper.WRBillerPlansRequest;
import totipay.wallet.di.RequestHelper.WRPayBillRequest;
import totipay.wallet.di.RequestHelper.WRPrepaidRechargeRequest;

public class MobileTopUpMainActivity extends TootiBaseActivity<ActivityMobileTopUpMainBinding> {

    NavController navController;
    public WRBillerPlansRequest plansRequest;
    public WRPayBillRequest payBillRequest;
    public GetWRPrepaidPlansRequest prepaidPlansRequest;
    public WRPrepaidRechargeRequest prepaidRechargeRequest;

    public Integer topUpType = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mobile_top_up_main;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.titleTxt.setText(getString(R.string.mobile_top_up));
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        plansRequest = new WRBillerPlansRequest();
        payBillRequest = new WRPayBillRequest();
        prepaidPlansRequest = new GetWRPrepaidPlansRequest();
        prepaidRechargeRequest = new WRPrepaidRechargeRequest();

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