package tootipay.wallet.billpayment;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import tootipay.wallet.R;
import tootipay.wallet.base.TootiBaseActivity;
import tootipay.wallet.databinding.ActivityBillMainBinding;
import tootipay.wallet.di.RequestHelper.WRBillerPlansRequest;
import tootipay.wallet.di.RequestHelper.WRPayBillRequest;

public class BillPaymentMainActivity extends TootiBaseActivity<ActivityBillMainBinding> {

    private NavController navController;
    public WRBillerPlansRequest plansRequest;
    public WRPayBillRequest payBillRequest;


    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_main;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
        plansRequest = new WRBillerPlansRequest();
        payBillRequest = new WRPayBillRequest();

        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.titleTxt.setText(getString(R.string.bill_payment_tool_title));

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
        if (navController.getCurrentDestination().getId() == R.id.utilityBillPaymentPlanFragment) {
            finish();
        } else {
            navController.navigateUp();
        }
    }
}