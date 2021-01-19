package tootipay.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import tootipay.wallet.R;
import tootipay.wallet.databinding.ActivityPaymentDoneBinding;
import tootipay.wallet.fragments.BaseFragment;

public class PaymentDoneActivity extends BaseFragment<ActivityPaymentDoneBinding> {

    @Override
    protected void injectView() {

    }

    @Override
    protected void setUp(Bundle savedInstanceState) {
        binding.viewReciept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_done;
    }


}