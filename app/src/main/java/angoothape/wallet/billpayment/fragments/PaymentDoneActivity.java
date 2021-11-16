package angoothape.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import angoothape.wallet.R;
import angoothape.wallet.databinding.ActivityPaymentDoneBinding;
import angoothape.wallet.fragments.BaseFragment;

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