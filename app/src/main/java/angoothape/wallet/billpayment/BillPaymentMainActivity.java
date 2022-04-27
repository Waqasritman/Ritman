package angoothape.wallet.billpayment;


import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.View;

import angoothape.wallet.R;
import angoothape.wallet.base.RitmanBaseActivity;
import angoothape.wallet.billpayment.viewmodel.BillPaymentViewModel;
import angoothape.wallet.databinding.ActivityBillMainBinding;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillDetailRequestN;
import angoothape.wallet.di.JSONdi.restRequest.GetWRBillerFieldsRequestN;
import angoothape.wallet.di.XMLdi.RequestHelper.WRBillerPlansRequest;
import angoothape.wallet.di.XMLdi.RequestHelper.WRPayBillRequest;

public class BillPaymentMainActivity extends RitmanBaseActivity<ActivityBillMainBinding> {

    private NavController navController;
    //public WRBillerPlansRequest plansRequest;
    public WRPayBillRequest payBillRequest;

    public BillPaymentViewModel viewModel;
    public GetWRBillerFieldsRequestN request;

    public GetWRBillDetailRequestN BillDetailRequest;


    @Override
    public int getLayoutId() {
        return R.layout.activity_bill_main;
    }


    @Override
    protected void initUi(Bundle savedInstanceState) {
        navController = Navigation.findNavController(this, R.id.dashboard);
        viewModel = new ViewModelProvider(this).get(BillPaymentViewModel.class);
        binding.toolBar.backBtn.setOnClickListener(v -> onBackPressed());
//        plansRequest = new WRBillerPlansRequest();
        payBillRequest = new WRPayBillRequest();
        request = new GetWRBillerFieldsRequestN();
        BillDetailRequest = new GetWRBillDetailRequestN();

        binding.toolBar.backBtn
                .setColorFilter(ContextCompat.getColor(this,
                        R.color.colorWhite), android.graphics.PorterDuff.Mode.SRC_IN);
        binding.toolBar.toolBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        binding.toolBar.titleTxt.setText(getString(R.string.bill_payment_tool_title));

        binding.toolBar.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.toolBar.crossBtn.setOnClickListener(v -> {
            onClose();
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.payBillFragment) {
                // binding.toolBar.toolBarFinal.setVisibility(View.GONE);
                binding.toolBar.bbpsToolbar.setVisibility(View.GONE);
                // bottomNavigationView.setVisibility(View.GONE);
            } else if (destination.getId() == R.id.paymentAmountValidationFragment) {
                binding.toolBar.bbpsToolbar.setVisibility(View.INVISIBLE);
            } else {
                binding.toolBar.bbpsToolbar.setVisibility(View.VISIBLE);
                //bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController.getCurrentDestination().getId() == R.id.utilityCategoryBFragment
                || navController.getCurrentDestination().getId() == R.id.payBillFragment) {
            finish();
        } else {
            navController.navigateUp();

        }

    }
}