package tootipay.wallet.billpayment.fragments;

import android.os.Bundle;

import androidx.navigation.Navigation;

import tootipay.wallet.R;
import tootipay.wallet.databinding.ActivitySelectBillPaymentModeBinding;
import tootipay.wallet.fragments.BaseFragment;

public class SelectBillPaymentModeActivity extends BaseFragment<ActivitySelectBillPaymentModeBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.wallet.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_selectBillPaymentModeActivity_to_paymentDoneActivity));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_bill_payment_mode;
    }


}