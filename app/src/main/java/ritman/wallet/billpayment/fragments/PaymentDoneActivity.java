package ritman.wallet.billpayment.fragments;

import android.os.Bundle;
import android.view.View;

import ritman.wallet.R;
import ritman.wallet.databinding.ActivityPaymentDoneBinding;
import ritman.wallet.fragments.BaseFragment;

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